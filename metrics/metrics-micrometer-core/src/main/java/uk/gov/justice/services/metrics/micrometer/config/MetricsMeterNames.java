package uk.gov.justice.services.metrics.micrometer.config;

public interface MetricsMeterNames {

    /**
     * Count of events received via the Event Topic
     */
    String EVENTS_RECEIVED_COUNTER_NAME = "framework.events.streams.events.received";

    /**
     * Count of all events passed to the Subscription Event Processor
     */
    String EVENTS_PROCESSED_COUNTER_NAME = "framework.events.streams.events.processed";

    /**
     * Count of all events successfully handled by the Event Handlers
     */
    String EVENTS_SUCCEEDED_COUNTER_NAME = "framework.events.streams.events.succeeded";

    /**
     * Count of all events ignored by the Subscription Event Processor
     */
    String EVENTS_IGNORED_COUNTER_NAME = "framework.events.streams.events.ignored.counter";

    /**
     * Count of all events that failed when handled by the Event Handlers
     */
    String EVENTS_FAILED_COUNTER_NAME = "framework.events.streams.events.failed.counter";

    /**
     * The current number of streams known about by the Subscription Status tables
     */
    String COUNT_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.totals.stream.count";

    /**
     * The current number of streams that are blocked due to errors
     */
    String BLOCKED_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.totals.blocked.streams.count";

    /**
     * The current number of streams that are out of date
     */
    String OUT_OF_DATE_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.totals.out.of.date.streams.count";

    /**
     * The current number of streams that are up-to-date
     */
    String UP_TO_DATE_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.totals.up.to.date.streams.count";
}
