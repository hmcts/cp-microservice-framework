package uk.gov.justice.services.persistence;

import static java.util.List.of;
import static uk.gov.justice.services.core.annotation.Component.EVENT_LISTENER;

import uk.gov.justice.services.core.interceptor.InterceptorChainEntry;
import uk.gov.justice.services.core.interceptor.InterceptorChainEntryProvider;

import java.util.List;

public class EntityManagerFlushInterceptorProvider implements InterceptorChainEntryProvider {

    private static final int PRIORITY = 220;

    @Override
    public String component() {
        return EVENT_LISTENER;
    }

    @Override
    public List<InterceptorChainEntry> interceptorChainTypes() {
        return of(new InterceptorChainEntry(PRIORITY, EntityManagerFlushInterceptor.class));
    }
}
