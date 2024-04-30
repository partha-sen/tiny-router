package com.tiny.router.model;

import java.lang.reflect.Method;

/**
 * RouteData is used to store routing information in HashMap as value.
 * @param <T> instance type.
 */
public class RouteData<T> {

    T instance;
    Method method;
    Class<?> parameterType;

    /**
     * Constrictor
     * @param instance object instance.
     * @param method generic method reference.
     * @param parameterType parameter Type object.
     */
    public RouteData(T instance, Method method, Class<?> parameterType) {
        this.instance = instance;
        this.method = method;
        this.parameterType = parameterType;
    }

    /**
     *
     * @return genetic type instance object.
     */
    public T getInstance() {
        return instance;
    }

    /**
     *
     * @return genetic method object.
     */
    public Method getMethod() {
        return method;
    }

    /**
     *
     * @return Class object.
     */

    public Class<?> getParameterType() {
        return parameterType;
    }

}
