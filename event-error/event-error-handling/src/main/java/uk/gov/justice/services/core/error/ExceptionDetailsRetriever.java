package uk.gov.justice.services.core.error;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ExceptionDetailsRetriever {

    @Inject
    private StackTraceProvider stackTraceProvider;

    public ExceptionDetails getExceptionDetailsFrom(final Throwable exception) {

        final List<StackTraceElement> stackTraceElements = new ArrayList<>();
        final String stackTrace = stackTraceProvider.getStackTraceFrom(exception);

        Throwable cause = exception;
        Throwable rootCause = null;
        while (cause != null) {

            final ArrayList<StackTraceElement> currentStackTraceElements = new ArrayList<>();
            for (final StackTraceElement stackTraceElement : cause.getStackTrace()) {
                final String className = stackTraceElement.getClassName();
                if (className.startsWith("uk.gov.justice")) {
                    currentStackTraceElements.add(stackTraceElement);
                }
            }

            stackTraceElements.addAll(0, currentStackTraceElements);

            cause = cause.getCause();

            if (cause != null) {
                rootCause = cause;
            }
        }

        return new ExceptionDetails(exception, rootCause, stackTraceElements, stackTrace);
    }
}
