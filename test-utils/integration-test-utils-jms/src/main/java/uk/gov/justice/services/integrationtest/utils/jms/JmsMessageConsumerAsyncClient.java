package uk.gov.justice.services.integrationtest.utils.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.function.Consumer;

public interface JmsMessageConsumerAsyncClient {
    void registerOnMessageListener(final Consumer<JsonEnvelope> onMessageCallBack);
}
