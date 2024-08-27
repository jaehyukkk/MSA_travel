package com.example.travel_api_gateway.context;

public class CustomSecurityContextHolder {
    private static final ThreadLocal<CustomSecurityContext> contextHolder = new ThreadLocal<>();

    public static void setContext(CustomSecurityContext context) {
        contextHolder.set(context);
    }

    public static CustomSecurityContext getContext() {
        return contextHolder.get();
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
