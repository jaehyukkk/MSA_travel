package com.example.travel_user_service.context

object CustomSecurityContextHolder {
    private val contextHolder = ThreadLocal<CustomSecurityContext>()

    fun setContext(context: CustomSecurityContext) {
        contextHolder.set(context)
    }

    fun getContext(): CustomSecurityContext? {
        return contextHolder.get()
    }

    fun clearContext() {
        contextHolder.remove()
    }
}
