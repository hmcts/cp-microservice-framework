package uk.gov.justice.services.integrationtest.utils.jms;

import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.queueUri;

import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.List;
import java.util.function.Consumer;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

public class DefaultJmsAsyncMessageConsumerAsyncClient implements JmsMessageConsumerAsyncClient {
    private static final String QUEUE_URI = queueUri();
    private final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;
    private final JmsMessageConsumerPool jmsMessageConsumerPool;
    private final JmsMessageReader jmsMessageReader;

    private MessageConsumer messageConsumer;

    DefaultJmsAsyncMessageConsumerAsyncClient(final JmsMessageConsumerPool jmsMessageConsumerPool,
                                              final JmsMessageReader jmsMessageReader,
                                              final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter) {
        this.jmsMessageConsumerPool = jmsMessageConsumerPool;
        this.jmsMessageReader = jmsMessageReader;
        this.toJsonEnvelopeMessageConverter = toJsonEnvelopeMessageConverter;
    }

    void startConsumer(final String topicName, final List<String> eventNames) {
        this.messageConsumer = jmsMessageConsumerPool.getOrCreateAsyncMessageConsumer(topicName, QUEUE_URI, eventNames);
    }

    @Override
    public void registerOnMessageListener(final Consumer<JsonEnvelope> onMessageCallBack) {
        try {
            jmsMessageReader.registerCallBack(messageConsumer, toJsonEnvelopeMessageConverter, onMessageCallBack);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to register message listener", e);
        }
    }
}
