package uk.gov.justice.services.metrics.micrometer.bootstrap;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class GaugeMeterRegistrar {

    public void registerGaugeMeter(final GaugeMetricsMeter metricsMeter, final MeterRegistry meterRegistry) {
        Gauge.builder(metricsMeter.metricName(), metricsMeter::measure)
                .tags(metricsMeter.metricTags())
                .description(metricsMeter.metricDescription())
                .register(meterRegistry);
    }
}
