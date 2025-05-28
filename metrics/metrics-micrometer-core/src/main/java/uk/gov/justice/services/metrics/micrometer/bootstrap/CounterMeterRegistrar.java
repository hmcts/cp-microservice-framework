package uk.gov.justice.services.metrics.micrometer.bootstrap;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class CounterMeterRegistrar {

    public void registerCounterMeter(final MetricsMeter metricsMeter, final MeterRegistry meterRegistry) {
        Counter.builder(metricsMeter.metricName())
                .tags(metricsMeter.metricTags())
                .description(metricsMeter.metricDescription())
                .register(meterRegistry);
    }
}
