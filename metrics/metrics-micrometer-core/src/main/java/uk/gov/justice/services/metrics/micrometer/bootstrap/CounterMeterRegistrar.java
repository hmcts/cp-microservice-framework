package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static java.lang.String.format;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import javax.inject.Inject;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

public class CounterMeterRegistrar {

    @Inject
    private Logger logger;

    public void registerCounterMeter(final MetricsMeter metricsMeter, final MeterRegistry meterRegistry) {
        logger.info(format("Registering Micrometer Counter '%s'", metricsMeter.metricName()));
        Counter.builder(metricsMeter.metricName())
                .tags(metricsMeter.metricTags())
                .description(metricsMeter.metricDescription())
                .register(meterRegistry);
    }
}
