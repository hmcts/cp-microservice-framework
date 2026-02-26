package uk.gov.justice.services.clients.core.httpclient;

public class HttpCallerException extends RuntimeException {

    public HttpCallerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
