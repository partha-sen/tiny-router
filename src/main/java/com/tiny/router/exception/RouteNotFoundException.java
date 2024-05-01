package com.tiny.router.exception;

/**
 * RouteNotFoundException class
 */
public class RouteNotFoundException extends RuntimeException {
    /**
     *
     * @param message exception message
     */
    public RouteNotFoundException(String message) {
        super(message);
    }
}
