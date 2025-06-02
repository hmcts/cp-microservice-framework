package uk.gov.justice.services.metrics.micrometer.counters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MicrometerMetricsCountersTest {

    @Mock
    private CompositeMeterRegistry compositeMeterRegistry;

    @InjectMocks
    private MicrometerMetricsCounters micrometerMetricsCounters;

    @Test
    public void shouldIncrementEventsReceivedCounter() throws Exception {

        final Counter eventsReceivedCounter = mock(Counter.class);
        when(compositeMeterRegistry.counter(EVENTS_RECEIVED_COUNTER_NAME)).thenReturn(eventsReceivedCounter);

        micrometerMetricsCounters.incrementEventsReceivedCount();

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsProcessedCounter() throws Exception {

        final Counter eventsProcessedCounter = mock(Counter.class);
        when(compositeMeterRegistry.counter(EVENTS_PROCESSED_COUNTER_NAME)).thenReturn(eventsProcessedCounter);

        micrometerMetricsCounters.incrementEventsProcessedCount();

        verify(eventsProcessedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsSucceededCounter() throws Exception {

        final Counter eventsSucceededCounter = mock(Counter.class);
        when(compositeMeterRegistry.counter(EVENTS_SUCCEEDED_COUNTER_NAME)).thenReturn(eventsSucceededCounter);

        micrometerMetricsCounters.incrementEventsSucceededCount();

        verify(eventsSucceededCounter).increment();
    }

    @Test
    public void shouldIncrementEventsIgnoredCounter() throws Exception {

        final Counter eventsIgnoredCounter = mock(Counter.class);
        when(compositeMeterRegistry.counter(EVENTS_IGNORED_COUNTER_NAME)).thenReturn(eventsIgnoredCounter);

        micrometerMetricsCounters.incrementEventsIgnoredCount();

        verify(eventsIgnoredCounter).increment();
    }

    @Test
    public void shouldIncrementEventsFailed() throws Exception {

        final Counter eventsFailedCounter = mock(Counter.class);
        when(compositeMeterRegistry.counter(EVENTS_FAILED_COUNTER_NAME)).thenReturn(eventsFailedCounter);

        micrometerMetricsCounters.incrementEventsFailedCount();

        verify(eventsFailedCounter).increment();
    }
}