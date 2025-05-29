package uk.gov.justice.services.metrics.micrometer.config;

public interface MetricsMeterNames {

    String EVENTS_RECEIVED_COUNTER_NAME = "events.received.counter";
    String EVENTS_PROCESSED_COUNTER_NAME = "events.processed.counter";
    String EVENTS_SUCCEEDED_COUNTER_NAME = "events.succeeded.counter";
    String EVENTS_IGNORED_COUNTER_NAME = "events.ignored.counter";
    String EVENTS_FAILED_COUNTER_NAME = "events.failed.counter";
    String COUNT_EVENT_STREAMS_GAUGE_NAME = "count.event.streams.gauge";
}
