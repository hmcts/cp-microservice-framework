package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

public class EventsProcessedCounterMeter implements MetricsMeter {

    @Override
    public String metricName() {
        return EVENTS_PROCESSED_COUNTER_NAME;
    }

    @Override
    public String metricDescription() {
        return "Count of all events passed to the Subscription Event Processor";
    }
}
