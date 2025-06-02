package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

public class EventsSucceededCounterMeter implements MetricsMeter {

    @Override
    public String metricName() {
        return EVENTS_SUCCEEDED_COUNTER_NAME;
    }

    @Override
    public String metricDescription() {
        return "Count of all events successfully handled by the Event Handlers";
    }
}
