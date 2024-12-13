package uk.gov.justice.subscription.jms.it;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.subscription.JsonEnvelopeProcessingFailureHandler;

public class DummyJsonEnvelopeProcessingFailureHandler implements JsonEnvelopeProcessingFailureHandler {

    @Override
    public void onJsonEnvelopeProcessingFailure(final JsonEnvelope jsonEnvelope, final Exception exception) {

    }
}
