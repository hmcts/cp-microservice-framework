package uk.gov.justice.services.metrics.micrometer.bootstrap;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;
import uk.gov.justice.services.metrics.micrometer.counters.EventsProcessedCounterMeter;
import uk.gov.justice.services.metrics.micrometer.meters.gauges.CountEventStreamsGaugeMeter;
import uk.gov.justice.services.metrics.micrometer.registry.MetricsRegistrar;

import javax.inject.Inject;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.slf4j.Logger;

public class MicrometerMetricsBootstrapper {

    @Inject
    private MetricsConfiguration metricsConfiguration;

    @Inject
    private CompositeMeterRegistry compositeMeterRegistry;

    @Inject
    private MetricsRegistrar metricsRegistrar;

    @Inject
    private CountEventStreamsGaugeMeter countEventStreamsGaugeMeter;

    @Inject
    private GaugeMeterRegistrar gaugeMeterRegistrar;

    @Inject
    private CounterMeterRegistrar counterMeterRegistrar;

    @Inject
    private EventsProcessedCounterMeter eventsProcessedCounterMeter;

    @Inject
    private Logger logger;

    public void bootstrapMetrics() {

        if (metricsConfiguration.micrometerMetricsEnabled()) {
            logger.info("Starting micrometer metrics");

            metricsRegistrar.addRegistry(compositeMeterRegistry);
            gaugeMeterRegistrar.registerGaugeMeter(countEventStreamsGaugeMeter, compositeMeterRegistry);
            counterMeterRegistrar.registerCounterMeter(eventsProcessedCounterMeter, compositeMeterRegistry);

        } else {
            logger.warn("Micrometer metrics disabled");
        }
    }
}
