package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class JmsMessagingClientExceptionTest {

    @Test
    void verifyWithMessage() {
        final JmsMessagingClientException e = new JmsMessagingClientException("An error occurred");

        assertThat(e.getMessage(), is("An error occurred"));
    }

    @Test
    void verifyWithMessageAndCause() {
        final JMSException cause = new JMSException("Test");

        final JmsMessagingClientException e = new JmsMessagingClientException("An error occurred", cause);

        assertThat(e.getMessage(), is("An error occurred"));
        assertThat(e.getCause(), is(cause));
    }
}