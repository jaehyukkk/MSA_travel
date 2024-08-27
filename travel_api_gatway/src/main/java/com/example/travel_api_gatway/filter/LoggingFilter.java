package com.example.travel_api_gatway.filter;

import com.example.travel_api_gatway.context.CustomSecurityContext;
import com.example.travel_api_gatway.context.CustomSecurityContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> implements Ordered {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 비동기환경에서 쓰레드 ID 대신 고유한 ID를 생성하여 사용
            String correlationId = generateCorrelationId();
            MDC.put("correlationId", correlationId);

            try {
                logRequest(exchange, config, correlationId);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logResponse(exchange, config, correlationId);
                        MDC.clear();
                    }));
        };
    }

    private void logRequest(ServerWebExchange exchange, Config config, String correlationId) throws JsonProcessingException {
        CustomSecurityContext context = CustomSecurityContextHolder.getContext();
        Map<String, Object> logData = new HashMap<>();

        if (config.isPreLogger()) {
            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            logData.put("method", request.getMethod());
            logData.put("path", request.getPath().value());
            logData.put("remoteAddress", request.getRemoteAddress());
            logData.put("headers", request.getHeaders());
            logData.put("correlationId", correlationId);
            if (context != null) {
                logData.put("userId", context.getId());
                logData.put("username", context.getUsername());
                logData.put("roles", context.getRoles());
            }

            if (!queryParams.isEmpty()) {
                logData.put("queryParams", getJsonQueryParams(queryParams));
            }

            logger.info("##Request Log: {}", logData);
        }
    }

    private void logResponse(ServerWebExchange exchange, Config config, String correlationId) {
        if (config.isPostLogger()) {
            ServerHttpResponse response = exchange.getResponse();
            Map<String, Object> logData = new HashMap<>();
            logData.put("statusCode", response.getStatusCode());
            logData.put("responseHeaders", response.getHeaders());
            logData.put("correlationId", correlationId);
            logger.info("##Response Log: {}", logData);
        }
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private String getJsonQueryParams(MultiValueMap<String, String> queryParams) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        queryParams.forEach((key, value) -> map.put(key, String.join(",", value)));
        return objectMapper.writeValueAsString(map);
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
