package uk.gov.justice.metrics.micrometer;

/**
 * Base class for all incrementing metrics counters, to allow us to inject in any of the
 * framework code. Override and annotate with '@Specializes' to allow for CDI magic and implement
 */
public class MetricsCounters {

    public void incrementEventsReceivedCount() {}
    public void incrementEventsProcessedCount() {}
    public void incrementEventsSucceededCount() {}
    public void incrementEventsIgnoredCount() {}
    public void incrementEventsFailedCount() {}
}
