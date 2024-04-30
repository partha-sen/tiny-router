package com.tiny.router.exception;

/**
 * PayloadConversionException class
 */
public class PayloadConversionException extends RuntimeException{
    /**
     *
     * @param message exception message
     * @param cause root cause
     */
    public PayloadConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
