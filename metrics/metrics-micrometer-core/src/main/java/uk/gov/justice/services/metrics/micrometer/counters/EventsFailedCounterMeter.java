package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

public class EventsFailedCounterMeter implements MetricsMeter {

    @Override
    public String metricName() {
        return EVENTS_FAILED_COUNTER_NAME;
    }

    @Override
    public String metricDescription() {
        return "Count of all events that failed when handled by the Event Handlers";
    }
}
