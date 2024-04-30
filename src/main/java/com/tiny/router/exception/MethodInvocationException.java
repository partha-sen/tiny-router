package com.tiny.router.exception;

/**
 * MethodInvocationException class
 */
public class MethodInvocationException extends RuntimeException{

    /**
     *
     * @param message exception message
     * @param cause root cause
     */
    public MethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
