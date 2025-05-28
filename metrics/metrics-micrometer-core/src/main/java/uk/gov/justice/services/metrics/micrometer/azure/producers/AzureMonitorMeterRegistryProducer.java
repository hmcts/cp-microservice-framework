package uk.gov.justice.services.metrics.micrometer.azure.producers;

import static io.micrometer.core.instrument.Clock.SYSTEM;

import uk.gov.justice.services.metrics.micrometer.azure.config.FrameworkAzureMonitorConfig;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;

@Singleton
public class AzureMonitorMeterRegistryProducer {

    @Inject
    private FrameworkAzureMonitorConfig frameworkAzureMonitorConfig;

    @Produces
    public AzureMonitorMeterRegistry meterRegistry() {
        return new AzureMonitorMeterRegistry(
                frameworkAzureMonitorConfig,
                SYSTEM);
    }
}
