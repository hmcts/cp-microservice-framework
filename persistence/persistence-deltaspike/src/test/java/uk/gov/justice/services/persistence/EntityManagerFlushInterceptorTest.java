package uk.gov.justice.services.persistence;

import static javax.transaction.Status.STATUS_ACTIVE;
import static javax.transaction.Status.STATUS_MARKED_ROLLBACK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import java.util.List;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.FlushEvent;
import org.hibernate.event.spi.FlushEventListener;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EntityManagerFlushInterceptorTest {

    @Mock
    private HibernateSessionHolder hibernateSessionHolder;

    @Mock
    private UserTransaction userTransaction;

    @InjectMocks
    private EntityManagerFlushInterceptor entityManagerFlushInterceptor;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlushEntityManagerOnSuccess() throws Exception {

        final FlushEventListener flushEventListener = mock(FlushEventListener.class);
        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext outgoingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenReturn(outgoingInterceptorContext);
        when(userTransaction.getStatus()).thenReturn(STATUS_ACTIVE);
        stubFlushEventListenerChain(flushEventListener);

        assertThat(entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain), is(outgoingInterceptorContext));

        verify(flushEventListener).onFlush(any(FlushEvent.class));
    }

    @Test
    public void shouldNotFlushIfTheTransactionIsNotActive() throws Exception {

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext outgoingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenReturn(outgoingInterceptorContext);
        when(userTransaction.getStatus()).thenReturn(STATUS_MARKED_ROLLBACK);

        assertThat(entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain), is(outgoingInterceptorContext));

        verify(interceptorChain).processNext(incomingInterceptorContext);
        verifyNoInteractions(hibernateSessionHolder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldStillFlushOnHandlerFailure() throws Exception {

        final RuntimeException runtimeException = new RuntimeException("Oops");
        final FlushEventListener flushEventListener = mock(FlushEventListener.class);

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenThrow(runtimeException);
        when(userTransaction.getStatus()).thenReturn(STATUS_ACTIVE);
        stubFlushEventListenerChain(flushEventListener);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain));

        assertThat(exception, is(sameInstance(runtimeException)));

        verify(interceptorChain).processNext(incomingInterceptorContext);
        verify(flushEventListener).onFlush(any(FlushEvent.class));
    }

    @Test
    public void shouldThrowEntityManagerFlushExceptionIfGettingStatusOfUserTransactionFails() throws Exception {

        final SystemException systemException = new SystemException("Ooops");

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext outgoingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenReturn(outgoingInterceptorContext);
        when(userTransaction.getStatus()).thenThrow(systemException);

        final EntityManagerFlushException entityManagerFlushException = assertThrows(
                EntityManagerFlushException.class,
                () -> entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain));

        assertThat(entityManagerFlushException.getCause(), is(systemException));
        assertThat(entityManagerFlushException.getMessage(), is("Failed to get status of UserTransaction"));
    }

    @Test
    public void shouldNotFlushIfSessionIsNull() throws Exception {

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext outgoingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenReturn(outgoingInterceptorContext);
        when(userTransaction.getStatus()).thenReturn(STATUS_ACTIVE);
        when(hibernateSessionHolder.getSession()).thenReturn(null);

        assertThat(entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain), is(outgoingInterceptorContext));
    }

    @SuppressWarnings("unchecked")
    private void stubFlushEventListenerChain(final FlushEventListener flushEventListener) {
        final EventSource session = mock(EventSource.class);
        final SessionFactoryImplementor sessionFactory = mock(SessionFactoryImplementor.class);
        final ServiceRegistryImplementor serviceRegistry = mock(ServiceRegistryImplementor.class);
        final EventListenerRegistry eventListenerRegistry = mock(EventListenerRegistry.class);
        final EventListenerGroup<FlushEventListener> eventListenerGroup = mock(EventListenerGroup.class);

        when(hibernateSessionHolder.getSession()).thenReturn(session);
        when(session.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(serviceRegistry);
        when(serviceRegistry.getService(EventListenerRegistry.class)).thenReturn(eventListenerRegistry);
        when(eventListenerRegistry.getEventListenerGroup(EventType.FLUSH)).thenReturn(eventListenerGroup);

        when(eventListenerGroup.listeners()).thenReturn(List.of(flushEventListener));
    }
}