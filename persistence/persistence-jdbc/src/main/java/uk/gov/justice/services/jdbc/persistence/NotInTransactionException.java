package uk.gov.justice.services.jdbc.persistence;

public class NotInTransactionException extends RuntimeException {

    public NotInTransactionException(final String message) {
        super(message);
    }
}
