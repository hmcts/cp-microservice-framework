package uk.gov.justice.services.persistence;

import static javax.transaction.Status.STATUS_ACTIVE;

import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.FlushEvent;
import org.hibernate.event.spi.FlushEventListener;

public class EntityManagerFlushInterceptor implements Interceptor {

    @Inject
    private HibernateSessionHolder hibernateSessionHolder;

    @Inject
    private UserTransaction userTransaction;

    @Override
    public InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain) {
        SafeAutoFlushEventListener.enable();
        try {
            return interceptorChain.processNext(interceptorContext);
        } finally {
            SafeAutoFlushEventListener.disable();
            flushEntityManager();
        }
    }

    private void flushEntityManager() {
        try {
            if (userTransaction.getStatus() == STATUS_ACTIVE) {
                final Session session = hibernateSessionHolder.getSession();
                if (session != null) {
                    final EventSource eventSource = (EventSource) session;
                    final FlushEvent flushEvent = new FlushEvent(eventSource);

                    final SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
                    final Iterable<FlushEventListener> listeners = sfi.getServiceRegistry()
                            .getService(EventListenerRegistry.class)
                            .getEventListenerGroup(EventType.FLUSH)
                            .listeners();

                    for (final FlushEventListener listener : listeners) {
                        listener.onFlush(flushEvent);
                    }
                }
            }
        } catch (final SystemException e) {
            throw new EntityManagerFlushException("Failed to get status of UserTransaction", e);
        }
    }
}