package com.example.travel_api_gatway.filter;

import com.example.travel_api_gatway.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
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

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Id", claims.getSubject())
                        .header("X-Username", claims.get("username", String.class))
                        .header("X-Roles", String.join(",", claims.get("roles", String.class)))
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

}
