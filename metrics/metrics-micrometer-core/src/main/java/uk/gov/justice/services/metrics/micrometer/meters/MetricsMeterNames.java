package uk.gov.justice.services.metrics.micrometer.meters;

public interface MetricsMeterNames {


    /**
     * The current number of streams known about by the Subscription Status tables
     */
    String TOTAL_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.total";

    /**
     * The current number of streams that are blocked due to errors
     */
    String BLOCKED_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.blocked";

    /**
     * The current number of streams not in error
     */
    String UNBLOCKED_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.unblocked";

    /**
     * The current number of streams that are out of date
     */
    String STALE_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.stale";

    /**
     * The current number of streams that are up-to-date
     */
    String FRESH_EVENT_STREAMS_GAUGE_NAME = "framework.events.streams.fresh";

    /**
     * Count of events received via the Event Topic
     */
    String EVENTS_RECEIVED_COUNTER_NAME = "framework.events.received";

    /**
     * Count of all events passed to the Subscription Event Processor
     */
    String EVENTS_PROCESSED_COUNTER_NAME = "framework.events.processed";

    /**
     * Count of all events successfully handled by the Event Handlers
     */
    String EVENTS_SUCCEEDED_COUNTER_NAME = "framework.events.succeeded";

    /**
     * Count of all events ignored by the Subscription Event Processor
     */
    String EVENTS_IGNORED_COUNTER_NAME = "framework.events.ignored";

    /**
     * Count of all events that failed when handled by the Event Handlers
     */
    String EVENTS_FAILED_COUNTER_NAME = "framework.events.failed";
}
