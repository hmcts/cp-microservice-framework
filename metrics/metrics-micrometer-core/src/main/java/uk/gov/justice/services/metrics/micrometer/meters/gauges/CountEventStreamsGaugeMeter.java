package uk.gov.justice.services.metrics.micrometer.meters.gauges;

import static java.lang.String.format;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.COUNT_EVENT_STREAMS_GAUGE_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;

import javax.inject.Inject;

import org.slf4j.Logger;

public class CountEventStreamsGaugeMeter implements GaugeMetricsMeter {

    @Inject
    private StreamCountRepository streamCountRepository;

    @Inject
    private Logger logger;

    @Override
    public int measure() {
        final Integer numberOfStreams = streamCountRepository.countStreams();

        if (logger.isDebugEnabled()) {
            logger.debug(format("Micrometer counting number of event streams. Number of streams: %d", numberOfStreams));
        }

        return numberOfStreams;
    }

    @Override
    public String metricName() {
         return COUNT_EVENT_STREAMS_GAUGE_NAME;
    }

    @Override
    public String metricDescription() {
        return "The total number of event streams";
    }
}
