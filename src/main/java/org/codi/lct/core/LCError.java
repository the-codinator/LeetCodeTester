package org.codi.lct.core;

/**
 * Used for any errors while executing tests.
 * Unchecked since if anything fails, we always report back to the user (we can't really handle any error)
 */
public class LCError extends RuntimeException {

    public LCError(String message) {
        super(message);
    }

    public LCError(String message, Throwable cause) {
        super(message, cause);
    }
}
