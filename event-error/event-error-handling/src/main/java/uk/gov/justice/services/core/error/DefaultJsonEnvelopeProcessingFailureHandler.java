package uk.gov.justice.services.core.error;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.subscription.JsonEnvelopeProcessingFailureHandler;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

public class DefaultJsonEnvelopeProcessingFailureHandler implements JsonEnvelopeProcessingFailureHandler {

    @Inject
    private Logger logger;

    @Inject
    private ExceptionDetailsRetriever exceptionDetailsRetriever;

    @Override
    public void onJsonEnvelopeProcessingFailure(final JsonEnvelope jsonEnvelope, final Exception exception) {

        final Metadata metadata = jsonEnvelope.metadata();
        final ExceptionDetails exceptionDetails = exceptionDetailsRetriever.getExceptionDetailsFrom(exception);

        final Throwable originalException = exceptionDetails.getOriginalException();
        final Throwable cause = exceptionDetails.getCause();

        final List<StackTraceElement> stackTraceElements = exceptionDetails.getStackTraceElements();
        final StackTraceElement stackTraceElement = stackTraceElements.get(stackTraceElements.size() - 1);

        final StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append("====================================================================================").append("\n");
        stringBuilder.append("eventName: ").append(metadata.name()).append("\n");
        stringBuilder.append("eventId: ").append(metadata.id()).append("\n");
        stringBuilder.append("streamId: ").append(metadata.streamId().orElse(null)).append("\n");
        stringBuilder.append("originalException: ").append(originalException).append("\n");
        stringBuilder.append("cause: ").append(cause).append("\n");
        stringBuilder.append("originalException: ").append(originalException).append("\n");
        stringBuilder.append("className: ").append(stackTraceElement.getClassName()).append("\n");
        stringBuilder.append("method: ").append(stackTraceElement.getClassName()).append("\n");
        stringBuilder.append("line number: ").append(stackTraceElement.getLineNumber()).append("\n");

        stringBuilder.append("====================================================================================").append("\n");
        stringBuilder.append("Full stack trace:").append("\n");
        stringBuilder.append(exceptionDetails.getFullStackTrace()).append("\n");

        stringBuilder.append("====================================================================================").append("\n");


        logger.error(stringBuilder.toString());
    }
}
