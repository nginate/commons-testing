package com.github.nginate.commons.testing;

/**
 * Single exception for any problems related to {@link Initializer initializer}
 *
 * @since 1.0
 */
public class ObjectInitializationException extends RuntimeException {

    public ObjectInitializationException(Throwable cause) {
        super(cause);
    }

    public ObjectInitializationException(String message) {
        super(message);
    }

    public ObjectInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
