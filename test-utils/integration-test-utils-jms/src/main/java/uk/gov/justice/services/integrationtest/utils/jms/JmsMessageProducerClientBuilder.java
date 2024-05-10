package uk.gov.justice.services.integrationtest.utils.jms;

public class JmsMessageProducerClientBuilder {
    private final JmsMessageClientFactory jmsMessageClientFactory;
    private final String topicName;

    public static JmsMessageProducerClientBuilder newPublicJmsMessageProducerClientBuilder() {
        return new JmsMessageProducerClientBuilder("jms.topic.public.event");
    }

    public static JmsMessageProducerClientBuilder newPrivateJmsMessageProducerClientBuilder(final String contextName) {
        return new JmsMessageProducerClientBuilder("jms.topic.%s.event".formatted(contextName));
    }

    private JmsMessageProducerClientBuilder(final String topicName) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = new JmsSingletonResourceProvider().getJmsMessageClientFactory();
    }

    public JmsMessageProducerClient build() {
        final JmsMessageProducerClient jmsMessageProducerClient = jmsMessageClientFactory.createJmsMessageProducerClient();
        jmsMessageProducerClient.createProducer(topicName);

        return jmsMessageProducerClient;
    }
}
