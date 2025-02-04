package uk.gov.justice.services.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EntityManagerFlushInterceptorTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EntityManagerFlushInterceptor entityManagerFlushInterceptor;

    @Test
    public void shouldCallFlushOnTheHibernateEntityManagerToCommitTheTransaction() throws Exception {

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext outgoingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenReturn(outgoingInterceptorContext);

        assertThat(entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain), is(outgoingInterceptorContext));

        final InOrder inOrder = inOrder(entityManager, interceptorChain);

        inOrder.verify(interceptorChain).processNext(incomingInterceptorContext);
        inOrder.verify(entityManager).flush();
    }

    @Test
    public void shouldAlwaysCallFlushEvenOnFailure() throws Exception {

        final RuntimeException runtimeException = new RuntimeException("Oops");

        final InterceptorContext incomingInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        when(interceptorChain.processNext(incomingInterceptorContext)).thenThrow(runtimeException);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entityManagerFlushInterceptor.process(incomingInterceptorContext, interceptorChain));

        assertThat(exception, is(sameInstance(runtimeException)));

        final InOrder inOrder = inOrder(interceptorChain, entityManager);

        inOrder.verify(interceptorChain).processNext(incomingInterceptorContext);
        inOrder.verify(entityManager).flush();
    }
}