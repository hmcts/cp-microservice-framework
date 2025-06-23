package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import javax.inject.Inject;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

public class MicrometerMetricsCounters {

    @Inject
    private CompositeMeterRegistry compositeMeterRegistry;

    @Inject
    private CounterTagFactory counterTagFactory;

    @Inject
    private MetricsConfiguration metricsConfiguration;

    public void incrementEventsReceivedCount(final String source, final String component) {
        incrementCounterByTag(source, component, EVENTS_RECEIVED_COUNTER_NAME);
    }

    public void incrementEventsProcessedCount(final String source, final String component) {
        incrementCounterByTag(source, component, EVENTS_PROCESSED_COUNTER_NAME);
    }

    public void incrementEventsSucceededCount(final String source, final String component) {
        incrementCounterByTag(source, component, EVENTS_SUCCEEDED_COUNTER_NAME);

    }

    public void incrementEventsIgnoredCount(final String source, final String component) {
        incrementCounterByTag(source, component, EVENTS_IGNORED_COUNTER_NAME);

    }

    public void incrementEventsFailedCount(final String source, final String component) {
        incrementCounterByTag(source, component, EVENTS_FAILED_COUNTER_NAME);
    }

    private void incrementCounterByTag(final String source, final String component, final String counterName) {
        if (metricsConfiguration.micrometerMetricsEnabled()) {
            compositeMeterRegistry.get(counterName)
                    .tags(counterTagFactory.getCounterTags(source, component))
                    .counter()
                    .increment();
        }
    }
}
