//package com.example.travel_api_gatway.filter;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            // JWT 토큰을 검증하고 필요한 정보 추출
//            Claims claims = Jwts.parser()
//                    .setSigningKey("your-signing-key") // 실제 키로 교체
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // HTTP 헤더에 인증 정보 추가
//            ServerHttpRequest modifiedRequest = request.mutate()
//                    .header("X-User-Id", claims.getSubject())
//                    .header("X-Username", claims.get("username", String.class))
//                    .header("X-Roles", String.join(",", claims.get("roles", List.class)))
//                    .build();
//
//            return chain.filter(exchange.mutate().request(modifiedRequest).build());
//        }
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}
