package uk.gov.justice.services.messaging.jms;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EnvelopeSenderSelector {

    @Inject
    private JmsSender jmsSender;

    @Inject
    private ShutteringStoreSender shutteringStoreSender;

    private AtomicBoolean suspended = new AtomicBoolean(false);

    public EnvelopeSender getEnvelopeSender() {

        if (suspended.get()) {
            return shutteringStoreSender;
        }

        return jmsSender;
    }

    public void setSuspended(final boolean shuttered) {
        this.suspended.set(shuttered);
    }
}
