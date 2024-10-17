package uk.gov.justice.services.integrationtest.utils.jms;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

public class JmsMessageConsumerAsyncClientProvider {
    private final JmsMessageClientFactory jmsMessageClientFactory;

    private final String topicName;
    private final List<String> eventNames = new ArrayList<>();

    public static JmsMessageConsumerAsyncClientProvider newPublicJmsMessageConsumerAsyncClientProvider() {
        return new JmsMessageConsumerAsyncClientProvider("jms.topic.public.event");
    }

    public static JmsMessageConsumerAsyncClientProvider newPrivateJmsMessageConsumerAsyncClientProvider(final String contextName) {
        return new JmsMessageConsumerAsyncClientProvider("jms.topic.%s.event".formatted(contextName));
    }

    @VisibleForTesting
    JmsMessageConsumerAsyncClientProvider(final String topicName, final JmsResourcesContext jmsResourcesContext) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = jmsResourcesContext.getJmsMessageClientFactory();
    }

    private JmsMessageConsumerAsyncClientProvider(final String topicName) {
        this(topicName, new JmsResourcesContextProvider().get());
    }

    public JmsMessageConsumerAsyncClientProvider withEventNames(final String eventName, final String... additionalEventNames) {
        if (eventName == null || eventName.isBlank()) {
            throw new JmsMessagingClientException("eventName must be supplied");
        }

        this.eventNames.add(eventName);

        if (additionalEventNames != null && additionalEventNames.length > 0) {
            this.eventNames.addAll(asList(additionalEventNames));
        }

        return this;
    }

    public JmsMessageConsumerAsyncClientProvider withEventNames(final List<String> eventNames) {
        if (eventNames == null || eventNames.isEmpty()) {
            throw new JmsMessagingClientException("eventNames must be supplied");
        }

        this.eventNames.addAll(eventNames);

        return this;
    }

    public JmsMessageConsumerAsyncClient getMessageConsumerAsyncClient() {
        if (eventNames.isEmpty()) {
            throw new JmsMessagingClientException("eventName(s) must be supplied");
        }

        final DefaultJmsAsyncMessageConsumerAsyncClient jmsMessageConsumerAsyncClient = jmsMessageClientFactory
                .createJmsMessageConsumerAsyncClient();

        jmsMessageConsumerAsyncClient.startConsumer(topicName, eventNames);

        return jmsMessageConsumerAsyncClient;
    }
}
