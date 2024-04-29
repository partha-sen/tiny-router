package com.tiny.router.model;

import java.lang.reflect.Method;

public class RouteValue<T> {

    T instance;
    Method method;
    Class<?> parameterType;

    public RouteValue(T instance, Method method, Class<?> parameterType) {
        this.instance = instance;
        this.method = method;
        this.parameterType = parameterType;
    }

    public T getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

}
