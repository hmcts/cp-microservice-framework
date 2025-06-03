package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

public class EventsReceivedCounterMeter implements MetricsMeter {

    @Override
    public String metricName() {
        return EVENTS_RECEIVED_COUNTER_NAME;
    }

    @Override
    public String metricDescription() {
        return "Count of events received via the Event Topic";
    }
}
