package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static java.lang.String.format;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;

import java.util.List;

import javax.inject.Inject;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;

public class GaugeMeterRegistrar {

    @Inject
    private Logger logger;

    public void registerGaugeMeter(final GaugeMetricsMeter gaugeMetricsMeter, final MeterRegistry meterRegistry, List<Tag> globalTags) {

        logger.info(format("Registering Micrometer Gauge '%s'", gaugeMetricsMeter.metricName()));
        Gauge.builder(gaugeMetricsMeter.metricName(), gaugeMetricsMeter::measure)
                .tags(globalTags)
                .description(gaugeMetricsMeter.metricDescription())
                .register(meterRegistry);
    }
}
