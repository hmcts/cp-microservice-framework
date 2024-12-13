package uk.gov.justice.services.core.error;

import java.util.List;

public class ExceptionDetails {

    private final Throwable originalException;
    private final Throwable cause;
    private final List<StackTraceElement> stackTraceElements;
    private final String fullStackTrace;

    public ExceptionDetails(
            final Throwable originalException,
            final Throwable cause,
            final List<StackTraceElement> stackTraceElements,
            final String fullStackTrace) {
        this.originalException = originalException;
        this.cause = cause;
        this.stackTraceElements = stackTraceElements;
        this.fullStackTrace = fullStackTrace;
    }

    public Throwable getOriginalException() {
        return originalException;
    }

    public Throwable getCause() {
        return cause;
    }

    public List<StackTraceElement> getStackTraceElements() {
        return stackTraceElements;
    }

    public String getFullStackTrace() {
        return fullStackTrace;
    }
}
