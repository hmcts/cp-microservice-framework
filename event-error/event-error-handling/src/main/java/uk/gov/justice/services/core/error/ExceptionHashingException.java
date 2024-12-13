package uk.gov.justice.services.core.error;

public class ExceptionHashingException extends RuntimeException {

    public ExceptionHashingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
