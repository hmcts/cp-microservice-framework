package uk.gov.justice.services.persistence;

public class SafeAutoFlushException extends RuntimeException {

    public SafeAutoFlushException(final String message, final Throwable cause) {
        super(message, cause);
    }
}