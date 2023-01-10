package uk.gov.justice.services.clients.core;

import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.messaging.DefaultJsonObjectEnvelopeConverter;
import uk.gov.justice.services.messaging.logging.TraceLogger;

import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.clients.core.webclient.WebTargetFactoryCreatorBuilder.aWebTargetFactoryCreator;
import static uk.gov.justice.services.test.utils.core.enveloper.EnveloperFactory.createEnveloper;

public class RestClientProcessorBuilder {

    private String appName;

    public static RestClientProcessorBuilder aRestClientProcessorBuilder() {
        return new RestClientProcessorBuilder();
    }

    public RestClientProcessorBuilder withAppName(final String appName) {
        this.appName = appName;
        return this;
    }

    public RestClientProcessor build() {
        final DefaultRestClientProcessor restClientProcessor = new DefaultRestClientProcessor();
        restClientProcessor.stringToJsonObjectConverter = new StringToJsonObjectConverter();
        restClientProcessor.jsonObjectEnvelopeConverter = new DefaultJsonObjectEnvelopeConverter();
        restClientProcessor.traceLogger = mock(TraceLogger.class);
        restClientProcessor.enveloper = createEnveloper();
        restClientProcessor.webTargetFactoryCreator = aWebTargetFactoryCreator(appName);

        return restClientProcessor;
    }
}