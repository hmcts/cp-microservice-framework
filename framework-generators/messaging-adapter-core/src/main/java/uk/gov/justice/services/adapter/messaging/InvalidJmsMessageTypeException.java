package uk.gov.justice.services.adapter.messaging;

public class InvalidJmsMessageTypeException extends RuntimeException {

    private static final long serialVersionUID = -3614445244093906063L;

    public InvalidJmsMessageTypeException(final String message) {
        super(message);
    }

    public InvalidJmsMessageTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
