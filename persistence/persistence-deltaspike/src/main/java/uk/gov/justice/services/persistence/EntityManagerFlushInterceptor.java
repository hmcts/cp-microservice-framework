package uk.gov.justice.services.persistence;

import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class EntityManagerFlushInterceptor implements Interceptor {

    @Inject
    private EntityManager entityManager;

    @Override
    public InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain) {
        try {
            return interceptorChain.processNext(interceptorContext);
        } finally {
            entityManager.flush();
        }
    }
}
