package uk.gov.justice.metrics.micrometer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MetricsCountersTest {

    @InjectMocks
    private MetricsCounters metricsCounters;

    @Test
    public void shouldDoNothingForIncrementEventsFailedCount() throws Exception {
        metricsCounters.incrementEventsFailedCount();
    }

    @Test
    public void shouldDoNothingForIncrementEventsIgnoredCount() throws Exception {
        metricsCounters.incrementEventsIgnoredCount();
    }

    @Test
    public void shouldDoNothingForIncrementEventsReceivedCoun() throws Exception {
        metricsCounters.incrementEventsReceivedCount();
    }

    @Test
    public void shouldDoNothingForEventsProcessedCount() throws Exception {
        metricsCounters.incrementEventsProcessedCount();
    }

    @Test
    public void shouldDoNothingForEventsSucceededCount() throws Exception {

        metricsCounters.incrementEventsSucceededCount();
    }
}