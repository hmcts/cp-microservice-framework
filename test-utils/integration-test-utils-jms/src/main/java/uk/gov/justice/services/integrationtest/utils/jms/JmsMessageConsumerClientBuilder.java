package uk.gov.justice.services.integrationtest.utils.jms;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class JmsMessageConsumerClientBuilder {
    private static final JmsMessageClientFactory jmsMessageClientFactory = createJmsMessageClientFactory();

    private final String topicName;
    private final List<String> eventNames = new ArrayList<>();

    public static JmsMessageConsumerClientBuilder newPublicJmsMessageConsumerClientBuilder() {
        return new JmsMessageConsumerClientBuilder("jms.topic.public.event");
    }

    public static JmsMessageConsumerClientBuilder newPrivateJmsMessageConsumerClientBuilder(final String contextName) {
        return new JmsMessageConsumerClientBuilder("jms.topic.%s.event".formatted(contextName));
    }

    public static JmsMessageConsumerClientBuilder newJmsMessageConsumerClientBuilder(final String topicName) {
        return new JmsMessageConsumerClientBuilder(topicName);
    }

    private JmsMessageConsumerClientBuilder(final String topicName) {
        this.topicName = topicName;
    }

    public JmsMessageConsumerClientBuilder withEventNames(final String eventName, final String...additionalEventNames) {
        this.eventNames.add(eventName);

        if(additionalEventNames != null && additionalEventNames.length > 0) {
            this.eventNames.addAll(asList(additionalEventNames));
        }

        return this;
    }

    public JmsMessageConsumerClient build() {
        if(eventNames.isEmpty()) {
            throw new JmsMessagingClientException("eventName(s) must be supplied");
        }

        final JmsMessageConsumerClient jmsMessageConsumerClient = jmsMessageClientFactory.createJmsMessageConsumerClient();
        jmsMessageConsumerClient.startConsumer(topicName, eventNames);

        return jmsMessageConsumerClient;
    }

    private static JmsMessageClientFactory createJmsMessageClientFactory() {
        return new JmsSingletonResourceProvider().getJmsMessageClientFactory();
    }
}
