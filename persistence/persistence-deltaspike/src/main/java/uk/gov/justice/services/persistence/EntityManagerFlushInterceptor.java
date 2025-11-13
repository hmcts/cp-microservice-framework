package uk.gov.justice.services.persistence;

import static javax.transaction.Status.STATUS_ACTIVE;

import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class EntityManagerFlushInterceptor implements Interceptor {

    @Inject
    private EntityManager entityManager;

    @Inject
    private UserTransaction userTransaction;

    @Override
    public InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain) {
        try {
            return interceptorChain.processNext(interceptorContext);
        } finally {
            flushEntityManager();
        }
    }

    private void flushEntityManager() {
        try {
            if (userTransaction.getStatus() == STATUS_ACTIVE) {
                entityManager.flush();
            }
        } catch (final SystemException e) {
            throw new EntityManagerFlushException("Failed to get status of UserTransaction", e);
        }
    }
}
