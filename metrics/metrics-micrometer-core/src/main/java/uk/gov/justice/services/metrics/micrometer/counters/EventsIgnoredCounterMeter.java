package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

public class EventsIgnoredCounterMeter implements MetricsMeter {

    @Override
    public String metricName() {
        return EVENTS_IGNORED_COUNTER_NAME;
    }

    @Override
    public String metricDescription() {
        return "Count of all events ignored by the Subscription Event Processor";
    }
}
