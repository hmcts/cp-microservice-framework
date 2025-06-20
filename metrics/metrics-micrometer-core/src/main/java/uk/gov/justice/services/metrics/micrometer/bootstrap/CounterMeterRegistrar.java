package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static java.lang.String.format;

import io.micrometer.core.instrument.Tag;
import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import javax.inject.Inject;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

import java.util.List;

public class CounterMeterRegistrar {

    @Inject
    private Logger logger;

    public void registerCounterMeter(final MetricsMeter metricsMeter, final MeterRegistry meterRegistry, List<Tag> globalTags) {
        logger.info(format("Registering Micrometer Counter '%s'", metricsMeter.metricName()));
        Counter.builder(metricsMeter.metricName())
                .tags(globalTags)
                .tags(metricsMeter.metricTags())
                .description(metricsMeter.metricDescription())
                .register(meterRegistry);
    }
}
