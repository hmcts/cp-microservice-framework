package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static io.micrometer.core.instrument.Clock.SYSTEM;
import static java.lang.String.format;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;
import uk.gov.justice.services.metrics.value.providers.StreamCountMetricsProvider;

import javax.inject.Inject;

import io.micrometer.azuremonitor.AzureMonitorConfig;
import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.slf4j.Logger;

public class MicrometerMetricsBootstrapper {

    @Inject
    private Logger logger;

    @Inject
    private MetricsConfiguration metricsConfiguration;

    public void bootstrapMetrics() {

        if (metricsConfiguration.micrometerMetricsEnabled()) {
            logger.info("Starting micrometer metrics");

            final CompositeMeterRegistry compositeRegistry = new CompositeMeterRegistry()
                    .add(azureMeterRegistry());

            Metrics.addRegistry(compositeRegistry);

            final String name = "test.micrometer.counter";
            final Counter counter = Counter
                    .builder(name)
                    .description("Simple test counter in people service")
                    .tags("peg", "my-little-pony")
                    .register(compositeRegistry);

            Gauge.builder("test.event.stream.count", new StreamCountMetricsProvider(), StreamCountMetricsProvider::countTotalNumberOfStreams)
                    .tags("peg", "my-little-pony")
                    .description("Gauge of number of existing streams")
                    .register(compositeRegistry);

            counter.increment(1.0);
            counter.increment(3.5);
            counter.increment(3.14);
            counter.increment(42);
            counter.increment(23);


            logger.info(format("Micrometer counter '%s' incremented to %.2f", name, counter.count()));

        } else {
            logger.warn("Micrometer metrics disabled");
        }
    }

    private MeterRegistry azureMeterRegistry() {
        final AzureMonitorConfig azureMonitorConfig = new AzureMonitorConfig() {
            @Override
            public String connectionString() {
                return "InstrumentationKey=4e87617c-31b6-4cfc-b831-9326d3ff2d47;IngestionEndpoint=https://uksouth-1.in.applicationinsights.azure.com/;LiveEndpoint=https://uksouth.livediagnostics.monitor.azure.com/;ApplicationId=1585c12b-b2f9-4d0a-b34e-82c7037ab673";
            }

            @Override
            public String get(final String key) {
                return null;
            }

            @Override
            public boolean enabled() {
                return metricsConfiguration.micrometerMetricsEnabled();
            }
        };

        return new AzureMonitorMeterRegistry(azureMonitorConfig, SYSTEM);
    }
}
