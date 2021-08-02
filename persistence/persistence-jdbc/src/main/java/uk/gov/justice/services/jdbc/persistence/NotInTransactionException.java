package uk.gov.justice.services.jdbc.persistence;

public class NotInTransactionException extends RuntimeException {

    public NotInTransactionException() {
        super("Attempting to execute a query outside of a transaction is not allowed");
    }
}
