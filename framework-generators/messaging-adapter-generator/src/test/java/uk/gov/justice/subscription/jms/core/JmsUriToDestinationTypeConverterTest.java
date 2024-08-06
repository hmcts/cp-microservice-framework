package uk.gov.justice.subscription.jms.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.core.annotation.Component.EVENT_LISTENER;
import static uk.gov.justice.services.core.annotation.Component.EVENT_PROCESSOR;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

import org.junit.jupiter.api.Test;

class JmsUriToDestinationTypeConverterTest {

    private JmsUriToDestinationTypeConverter jmsUriToDestinationTypeConverter = new JmsUriToDestinationTypeConverter();

    @Test
    public void shouldConvertJmsTopicUriToDestinationType() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:topic:public.event").orElse(null);

        assertThat(destinationType, is(Topic.class));
    }

    @Test
    public void shouldConvertJmsTopicUriToDestinationTypeCaseInsensitive() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:toPIc:public.event").orElse(null);

        assertThat(destinationType, is(Topic.class));
    }

    @Test
    public void shouldConvertJmsQueueUriToDestinationType() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:queue:command.handler").orElse(null);

        assertThat(destinationType, is(Queue.class));
    }

    @Test
    public void shouldConvertJmsQueueUriToDestinationTypeCaseInsensitive() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:quEUe:command.handler").orElse(null);

        assertThat(destinationType, is(Queue.class));
    }

    @Test
    public void shouldConvertJmsInvalidUriToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:xxqueue:command.handler").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertHelloJmsdUriToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("hello").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertEmptyJmsdUriToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertNullJmsdUriToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert(null).orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertJmsTopicInPositionZeroToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("topic:jms:public.event").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertJmsTopicInPositionTwoToDestinationTypeNull() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convert("jms:alpha:topic:public.event").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }

    @Test
    public void shouldConvertForEventProcessor() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convertForEventProcessor(EVENT_PROCESSOR, "jms:topic:public.event").orElse(null);

        assertThat(destinationType, is(Topic.class));
    }

    @Test
    public void shouldNotConvertForEventProcessorIfComponentIsEventListener() {

        final Class<? extends Destination> destinationType = jmsUriToDestinationTypeConverter.convertForEventProcessor(EVENT_LISTENER, "jms:topic:public.event").orElse(null);

        assertThat(destinationType, is(nullValue()));
    }
}