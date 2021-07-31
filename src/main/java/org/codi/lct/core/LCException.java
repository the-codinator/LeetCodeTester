package org.codi.lct.core;

/**
 * Used for any errors while executing tests.
 * Unchecked since if anything fails, we always report back to the user (we can't really handle any error)
 */
public final class LCException extends RuntimeException {

    public LCException(String message) {
        super(message);
    }

    public LCException(String message, Throwable cause) {
        super(message, cause);
    }
}
