package uk.gov.justice.services.integrationtest.utils.jms;

import io.restassured.path.json.JsonPath;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.queueUri;

//TODO can this be renamed as Subscription?
/**
 * Use {@link JmsMessageConsumerClientBuilder} to create instance
 * It's safe to create multiple instances of this class with same parameters, as underlying jms consumer is cached and it retrieves existing consumer
 * It's not recommended to cache these classes across Test classes, reusability is recommended only within same Test class. After each Test class all cached consumers are closed.
 * Hence, don't create instances of this class in Helper/Utility classes and assign it to static class variables. Doing this within a Test class is totally fine though
 * Life cycle of underlying jms consumer is not managed by this class (Managed by {@link JmsMessageConsumerPool} through Junit hooks {@link JmsResourceManagementExtension}) and hence these instances can be created without worrying about cleaning underlying jms resources
 * This class provides all various helper methods to retrieve message from underlying subscription queue
 * If there is no convenient method that you are looking for, please enhance this class rather than creating them in context ITs. This approach avoids duplication and promotes reusability across different context Integration tests
 */
public class JmsMessageConsumerClient {

    private static final long TIMEOUT_IN_MILLIS = 20_000;
    private static final String QUEUE_URI = queueUri();
    private final ToStringMessageConverter toStringMessageConverter;
    private final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;
    private final ToJsonPathMessageConverter toJsonPathMessageConverter;

    private final JmsMessageConsumerPool jmsMessageConsumerPool;
    private final JmsMessageReader jmsMessageReader;

    private MessageConsumer messageConsumer;

    JmsMessageConsumerClient(final JmsMessageConsumerPool jmsMessageConsumerPool,
                             final JmsMessageReader jmsMessageReader,
                             final ToStringMessageConverter toStringMessageConverter,
                             final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter,
                             final ToJsonPathMessageConverter toJsonPathMessageConverter) {
        this.jmsMessageConsumerPool = jmsMessageConsumerPool;
        this.jmsMessageReader = jmsMessageReader;
        this.toStringMessageConverter = toStringMessageConverter;
        this.toJsonEnvelopeMessageConverter = toJsonEnvelopeMessageConverter;
        this.toJsonPathMessageConverter = toJsonPathMessageConverter;
    }

    void startConsumer(final String topicName, final List<String> eventNames) {
        this.messageConsumer = jmsMessageConsumerPool.getOrCreateMessageConsumer(topicName, QUEUE_URI, eventNames);
    }

    public Optional<String> retrieveMessageNoWait() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessageNoWait(messageConsumer, toStringMessageConverter);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    public Optional<String> retrieveMessage() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        return retrieveMessage(TIMEOUT_IN_MILLIS);
    }

    public Optional<String> retrieveMessage(final long timeout) {
        //TODO Refactor to extract common code
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessage(messageConsumer, toStringMessageConverter, timeout);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    //Add more convenient methods
    public Optional<JsonEnvelope> retrieveMessageAsJsonEnvelopeNoWait() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessageNoWait(messageConsumer, toJsonEnvelopeMessageConverter);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    public Optional<JsonEnvelope> retrieveMessageAsJsonEnvelope() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        return retrieveMessageAsJsonEnvelope(TIMEOUT_IN_MILLIS);
    }

    public Optional<JsonEnvelope> retrieveMessageAsJsonEnvelope(final long timeout) {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessage(messageConsumer, toJsonEnvelopeMessageConverter, timeout);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    public Optional<JsonPath> retrieveMessageAsJsonPathNoWait() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessageNoWait(messageConsumer, toJsonPathMessageConverter);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    public Optional<JsonPath> retrieveMessageAsJsonPath() {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        return retrieveMessageAsJsonPath(TIMEOUT_IN_MILLIS);
    }

    public Optional<JsonPath> retrieveMessageAsJsonPath(final long timeout) {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        try {
            return jmsMessageReader.retrieveMessage(messageConsumer, toJsonPathMessageConverter, timeout);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to read message", e);
        }
    }

    public List<JsonPath> retrieveMessagesAsJsonPath(final int expectedCount) {
        if(messageConsumer == null) {
            throw new JmsMessagingClientException("MessageConsumer is not started. Please call startConsumer(...) first");
        }

        final List<JsonPath> retrievedMessages = new ArrayList<>();

        for(int cnt=0;cnt<expectedCount;cnt++) {
            retrievedMessages.add(retrieveMessageAsJsonPath().orElse(null));
        }

        return retrievedMessages;
    }

    //This can be used if there is a requirement to clear a specific queue while test is in progress
    public void clearMessages() {
        try {
            jmsMessageReader.clear(messageConsumer);
        } catch (JMSException e) {
            throw new JmsMessagingClientException("Failed to clear messages", e);
        }
    }
}
