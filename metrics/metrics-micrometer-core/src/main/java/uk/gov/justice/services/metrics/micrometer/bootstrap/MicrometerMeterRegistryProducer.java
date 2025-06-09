package uk.gov.justice.services.metrics.micrometer.bootstrap;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

@Singleton
public class MicrometerMeterRegistryProducer {

    @Inject
    private AzureMonitorMeterRegistryFactory azureMonitorMeterRegistryFactory;

    @Inject
    private MetricsConfiguration metricsConfiguration;

    @Inject
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @Inject
    private CompositeMeterRegistryFactory compositeMeterRegistryFactory;

    private CompositeMeterRegistry compositeMeterRegistry;


    @Produces
    public synchronized CompositeMeterRegistry compositeMeterRegistry() {

        if (compositeMeterRegistry == null) {
            compositeMeterRegistry = compositeMeterRegistryFactory.createNew();

            if(metricsConfiguration.micrometerMetricsEnabled()) {
                final AzureMonitorMeterRegistry azureMonitorMeterRegistry = azureMonitorMeterRegistryFactory.create();

                compositeMeterRegistry.add(prometheusMeterRegistry);
                compositeMeterRegistry.add(azureMonitorMeterRegistry);
            }
        }

        return compositeMeterRegistry;
    }
}
