package uk.gov.justice.services.persistence;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultAutoFlushEventListener;
import org.hibernate.event.spi.AutoFlushEvent;

public class SafeAutoFlushEventListener extends DefaultAutoFlushEventListener {

    private static final ThreadLocal<Boolean> enabled = ThreadLocal.withInitial(() -> Boolean.FALSE);

    public static void enable() {
        enabled.set(Boolean.TRUE);
    }

    public static void disable() {
        enabled.remove();
    }

    @Override
    public void onAutoFlush(final AutoFlushEvent event) throws HibernateException {
        if (enabled.get()) {
            try {
                super.onAutoFlush(event);
            } catch (final RuntimeException e) {
                throw new SafeAutoFlushException("Auto-flush failed: " + e.getMessage(), e);
            }
        } else {
            super.onAutoFlush(event);
        }
    }
}