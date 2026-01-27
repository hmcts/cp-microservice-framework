package uk.gov.justice.services.adapter.messaging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.subscription.pull.EventPullConfiguration;
import uk.gov.justice.services.subscription.SubscriptionManager;

import javax.jms.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultSubscriptionJmsProcessorTest {

    @Mock
    private EventPullConfiguration eventPullConfiguration;

    @Mock
    private SubscriptionJmsProcessorDelegate subscriptionJmsProcessorDelegate;

    @InjectMocks
    private DefaultSubscriptionJmsProcessor subscriptionJmsProcessor;

    @Test
    public void shouldProcessMessageIfProcessEventsFromEventTopicJndiValueIsTrue() throws Exception {

        final TextMessage message = mock(TextMessage.class);
        final SubscriptionManager subscriptionManager = mock(SubscriptionManager.class);

        when(eventPullConfiguration.shouldProcessEventsFromEventTopic()).thenReturn(true);

        subscriptionJmsProcessor.process(message, subscriptionManager);

        verify(subscriptionJmsProcessorDelegate).process(message, subscriptionManager);
    }

    @Test
    public void shouldDoNothingWithMessageIfProcessEventsFromEventTopicJndiValueIsFalse() throws Exception {

        final TextMessage message = mock(TextMessage.class);
        final SubscriptionManager subscriptionManager = mock(SubscriptionManager.class);

        when(eventPullConfiguration.shouldProcessEventsFromEventTopic()).thenReturn(false);

        subscriptionJmsProcessor.process(message, subscriptionManager);

        verify(subscriptionJmsProcessorDelegate, never()).process(message, subscriptionManager);
    }
}