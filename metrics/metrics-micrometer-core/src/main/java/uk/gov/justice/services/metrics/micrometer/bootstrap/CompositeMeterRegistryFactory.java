package uk.gov.justice.services.metrics.micrometer.bootstrap;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

public class CompositeMeterRegistryFactory {

    public CompositeMeterRegistry createNew() {
        return new CompositeMeterRegistry();
    }
}
