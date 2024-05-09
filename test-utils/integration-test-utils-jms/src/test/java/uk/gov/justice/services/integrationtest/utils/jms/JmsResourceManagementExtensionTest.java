package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getField;

class JmsResourceManagementExtensionTest {

    private JmsMessageConsumerPool jmsMessageConsumerPool;

    private JmsMessageProducerFactory jmsMessageProducerFactory;

    private final JmsResourceManagementExtension jmsResourceManagementExtension = new JmsResourceManagementExtension();

    @BeforeEach
    void setUp() throws Exception {
        jmsMessageConsumerPool = mock(JmsMessageConsumerPool.class);
        jmsMessageProducerFactory = mock(JmsMessageProducerFactory.class);
        //Not nice to use reflection to set static field values, but defining constructor enforces to pass those arguments when using that extension in junit tests
        //TODO try defining no args constructor and a private arguments constructor for tests and see if it still works
        setStaticField(JmsResourceManagementExtension.class, "jmsMessageConsumerPool", jmsMessageConsumerPool);
        setStaticField(JmsResourceManagementExtension.class, "jmsMessageProducerFactory", jmsMessageProducerFactory);
    }

    @Test
    void clearMessagesBeforeEachTestMethod() {
        jmsResourceManagementExtension.beforeEach(null);

        verify(jmsMessageConsumerPool).clearMessages();
    }

    @Test
    void closeAllMessageConsumersAfterAllTestsInATestClass() {
        jmsResourceManagementExtension.afterAll(null);

        verify(jmsMessageConsumerPool).closeConsumers();
    }

    @Test
    void closeConsumerPoolAndProducerFactoryAfterCompletionOfTestSuite() {
        jmsResourceManagementExtension.close();

        verify(jmsMessageConsumerPool).close();
        verify(jmsMessageProducerFactory).close();
    }

    @Test
    void registerShutdownMethodThroughBeforeAllHook() throws Exception {
        setStaticField(JmsResourceManagementExtension.class, "registered", false);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);
        final ExtensionContext rootContext = mock(ExtensionContext.class);
        final ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extensionContext.getRoot()).thenReturn(rootContext);
        when(rootContext.getStore(GLOBAL)).thenReturn(store);

        jmsResourceManagementExtension.beforeAll(extensionContext);

        verify(store).put("Clean JMS resources", jmsResourceManagementExtension);
    }

    @Test
    void doNotRegisterShutdownMethodIfItAlreadyRegistered() throws Exception {
        setStaticField(JmsResourceManagementExtension.class, "registered", true);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        jmsResourceManagementExtension.beforeAll(extensionContext);

        verifyNoInteractions(extensionContext);
    }

    private void setStaticField(final Class<?> clazz, final String fieldName, final Object value) throws Exception {
        final Field field = getField(clazz, fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}