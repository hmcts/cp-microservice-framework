package uk.gov.justice.services.messaging.jms;

import static jakarta.jms.Session.AUTO_ACKNOWLEDGE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.jms.exception.JmsEnvelopeSenderException;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuditJmsSenderTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private DestinationProvider destinationProvider;

    @Mock
    private EnvelopeConverter envelopeConverter;

    @InjectMocks
    private AuditJmsSender jmsSender;

    @Test
    public void shouldSendJsonEnvelopeToJmsQueue() throws Exception {

        final String destinationName = "destination name";

        final Connection connection = mock(Connection.class);
        final Session session = mock(Session.class);
        final MessageProducer messageProducer = mock(MessageProducer.class);
        final Destination destination = mock(Destination.class);
        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final TextMessage textMessage = mock(TextMessage.class);

        when(destinationProvider.getDestination(destinationName)).thenReturn(destination);
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createProducer(destination)).thenReturn(messageProducer);
        when(envelopeConverter.toMessage(jsonEnvelope, session)).thenReturn(textMessage);

        jmsSender.send(jsonEnvelope, destinationName);

        verify(messageProducer).send(textMessage);
    }

    @Test
    public void shouldThrowExceptionIfJmsExceptionIsThrown() throws Exception {

        final String destinationName = "destination name";

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);
        final Destination destination = mock(Destination.class);

        when(destinationProvider.getDestination(destinationName)).thenReturn(destination);
        when(connectionFactory.createConnection()).thenThrow(new JMSException("Test exception"));
        when(jsonEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn("command.test");

        try {
            jmsSender.send(jsonEnvelope, destinationName);
            fail();
        } catch (final JmsEnvelopeSenderException e) {
            assertThat(e.getMessage(), is("Exception while sending envelope with name command.test"));
        }
    }
}
