package uk.gov.justice.services.metrics.micrometer.registry;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;

public class MetricsRegistrar {

    public void addRegistry(final MeterRegistry meterRegistry) {
        Metrics.addRegistry(meterRegistry);
    }
}
