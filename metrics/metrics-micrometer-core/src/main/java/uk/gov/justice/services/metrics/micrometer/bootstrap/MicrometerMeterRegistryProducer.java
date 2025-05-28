package uk.gov.justice.services.metrics.micrometer.bootstrap;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

@Singleton
public class MicrometerMeterRegistryProducer {

    @Inject
    private AzureMonitorMeterRegistry azureMonitorMeterRegistry;

    @Inject
    private PrometheusMeterRegistry prometheusMeterRegistry;

    private CompositeMeterRegistry compositeMeterRegistry;

    @Produces
    public synchronized CompositeMeterRegistry compositeMeterRegistry() {

        if (compositeMeterRegistry == null) {
            compositeMeterRegistry = new CompositeMeterRegistry()
                    .add(azureMonitorMeterRegistry)
                    .add(prometheusMeterRegistry);
        }

        return compositeMeterRegistry;
    }
}
