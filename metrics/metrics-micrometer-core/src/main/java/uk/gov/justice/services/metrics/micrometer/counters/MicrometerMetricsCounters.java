package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import javax.inject.Inject;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

public class MicrometerMetricsCounters {

    @Inject
    private CompositeMeterRegistry compositeMeterRegistry;

    public void incrementEventsReceivedCount() {
        compositeMeterRegistry.counter(EVENTS_RECEIVED_COUNTER_NAME).increment();
    }

    public void incrementEventsProcessedCount() {
        compositeMeterRegistry.counter(EVENTS_PROCESSED_COUNTER_NAME).increment();
    }

    public void incrementEventsSucceededCount() {
        compositeMeterRegistry.counter(EVENTS_SUCCEEDED_COUNTER_NAME).increment();

    }

    public void incrementEventsIgnoredCount() {
        compositeMeterRegistry.counter(EVENTS_IGNORED_COUNTER_NAME).increment();

    }

    public void incrementEventsFailedCount() {
        compositeMeterRegistry.counter(EVENTS_FAILED_COUNTER_NAME).increment();
    }
}
