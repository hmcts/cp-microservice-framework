package uk.gov.justice.services.messaging.jms;

import static java.lang.String.format;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.jms.annotation.ConnectionFactoryName;
import uk.gov.justice.services.messaging.jms.exception.JmsEnvelopeSenderException;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class AuditJmsSender implements EnvelopeSender {

    @Inject
    @ConnectionFactoryName("java:comp/AuditJMSConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Inject
    private DestinationProvider destinationProvider;

    @Inject
    private EnvelopeConverter envelopeConverter;

    @Override
    public void send(final JsonEnvelope jsonEnvelope, final String destinationName) {

        final Destination destination = destinationProvider.getDestination(destinationName);

        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(destination)) {

            producer.send(envelopeConverter.toMessage(jsonEnvelope, session));

        } catch (final JMSException e) {
            throw new JmsEnvelopeSenderException(format("Exception while sending envelope with name %s", jsonEnvelope.metadata().name()), e);
        }
    }
}
