package com.example.travel_api_gateway.filter;

import com.example.travel_api_gateway.context.CustomSecurityContext;
import com.example.travel_api_gateway.context.CustomSecurityContextHolder;
import com.example.travel_api_gateway.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> implements Ordered {
    private final JwtUtil jwtUtil;
    public GlobalFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                System.out.println("Authorization header is missing");
                return chain.filter(exchange);
            }

            try {
                String jwt = authorizationHeader.substring(7);

                if (!jwtValid(jwt)) {
                    System.out.println("Authorization header is missing1");
                    return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
                }

                Claims claims = jwtUtil.getAllClaimsFromToken(jwt);

                String subject = claims.getSubject();
                String username = claims.get("username", String.class);
                String roles = String.join(",", claims.get("roles", String.class));

                CustomSecurityContextHolder.setContext(CustomSecurityContext.builder()
                        .id(Long.parseLong(subject))
                        .username(username)
                        .roles(roles)
                        .build());

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Id", subject)
                        .header("X-Username", username)
                        .header("X-Roles", roles)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                System.out.println("Authorization header is missing2");
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private boolean jwtValid(String jwt) {
        try {
            String subject = jwtUtil.getUsernameFromToken(jwt);
            return subject != null && !subject.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
