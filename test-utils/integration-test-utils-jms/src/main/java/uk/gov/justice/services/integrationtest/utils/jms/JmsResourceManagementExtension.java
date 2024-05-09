package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 *  This manages Life cycle of Jms consumers and producers created by a Test class using various junit hooks.
 *  As long as this extension is used by an Integration test, as a developer of writing integration tests you don't need to worry about managing underlying jms resources that are created in Tests
 */
public class JmsResourceManagementExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

    private static final JmsMessageConsumerPool jmsMessageConsumerPool = getJmsMessageConsumerPool();

    private static final JmsMessageProducerFactory jmsMessageProducerFactory = getJmsMessageProducerFactory();

    private static boolean registered = false;

    @Override
    public void beforeAll(final ExtensionContext context) {
        if (!registered) { //Not a nice way but there is no other elegant way to achieve this i.e. register hook only once
            registered = true;
            context.getRoot().getStore(GLOBAL).put("Clean JMS connections", this);
        }
    }

    /**
     * Drains all consumer queues created during test, so that message consumers can be reused across other tests within same Test class
     */
    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        jmsMessageConsumerPool.clearMessages();
    }

    /**
     * Closes all consumers that are created across all tests within a single Test class.  So, consumers can't be reused across different Test classes (Without this recycling, number of message consumers may outgrow and can cause resource exhaustion)
     */
    @Override
    public void afterAll(final ExtensionContext extensionContext) {
        jmsMessageConsumerPool.closeConsumers();
    }

    /**
     * This is invoked at the end of test suite (through hook registered in beforeAll() method).
     * This promotes reusing jms session and connection across entire test suite
     */
    @Override
    public void close() {
        jmsMessageConsumerPool.close();
        jmsMessageProducerFactory.close();
    }

    private static JmsMessageConsumerPool getJmsMessageConsumerPool() {
        return new JmsSingletonResourceProvider().getJmsMessageConsumerPool();
    }

    private static JmsMessageProducerFactory getJmsMessageProducerFactory() {
        return new JmsSingletonResourceProvider().getJmsMessageProducerFactory();
    }
}
