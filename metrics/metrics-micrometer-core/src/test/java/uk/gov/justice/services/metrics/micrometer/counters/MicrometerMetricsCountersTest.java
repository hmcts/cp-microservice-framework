package uk.gov.justice.services.metrics.micrometer.counters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import java.util.List;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.search.RequiredSearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MicrometerMetricsCountersTest {

    @Mock
    private CompositeMeterRegistry compositeMeterRegistry;

    @Mock
    private CounterTagFactory counterTagFactory;

    @Mock
    private MetricsConfiguration metricsConfiguration;

    @InjectMocks
    private MicrometerMetricsCounters micrometerMetricsCounters;

    @Test
    public void shouldIncrementEventsReceivedCounter() {
        final String source = "some-source";
        final String component = "some-component";

        final Counter eventsReceivedCounter = mock(Counter.class);
        final RequiredSearch requiredSearch = mock(RequiredSearch.class);
        final List<Tag> tags = List.of(mock(Tag.class), mock(Tag.class));

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(compositeMeterRegistry.get(EVENTS_RECEIVED_COUNTER_NAME)).thenReturn(requiredSearch);
        when(requiredSearch.counter()).thenReturn(eventsReceivedCounter);
        when(counterTagFactory.getCounterTags(source, component)).thenReturn(tags);
        when(requiredSearch.tags(tags)).thenReturn(requiredSearch);

        micrometerMetricsCounters.incrementEventsReceivedCount(source, component);

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsProcessedCounter() {
        final String source = "some-source";
        final String component = "some-component";

        final Counter eventsReceivedCounter = mock(Counter.class);
        final RequiredSearch requiredSearch = mock(RequiredSearch.class);
        final List<Tag> tags = List.of(mock(Tag.class), mock(Tag.class));

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(compositeMeterRegistry.get(EVENTS_PROCESSED_COUNTER_NAME)).thenReturn(requiredSearch);
        when(requiredSearch.counter()).thenReturn(eventsReceivedCounter);
        when(counterTagFactory.getCounterTags(source, component)).thenReturn(tags);
        when(requiredSearch.tags(tags)).thenReturn(requiredSearch);

        micrometerMetricsCounters.incrementEventsProcessedCount(source, component);

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsSucceededCounter() {
        final String source = "some-source";
        final String component = "some-component";

        final Counter eventsReceivedCounter = mock(Counter.class);
        final RequiredSearch requiredSearch = mock(RequiredSearch.class);
        final List<Tag> tags = List.of(mock(Tag.class), mock(Tag.class));

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(compositeMeterRegistry.get(EVENTS_SUCCEEDED_COUNTER_NAME)).thenReturn(requiredSearch);
        when(requiredSearch.counter()).thenReturn(eventsReceivedCounter);
        when(counterTagFactory.getCounterTags(source, component)).thenReturn(tags);
        when(requiredSearch.tags(tags)).thenReturn(requiredSearch);

        micrometerMetricsCounters.incrementEventsSucceededCount(source, component);

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsIgnoredCounter() {
        final String source = "some-source";
        final String component = "some-component";

        final Counter eventsReceivedCounter = mock(Counter.class);
        final RequiredSearch requiredSearch = mock(RequiredSearch.class);
        final List<Tag> tags = List.of(mock(Tag.class), mock(Tag.class));

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(compositeMeterRegistry.get(EVENTS_IGNORED_COUNTER_NAME)).thenReturn(requiredSearch);
        when(requiredSearch.counter()).thenReturn(eventsReceivedCounter);
        when(counterTagFactory.getCounterTags(source, component)).thenReturn(tags);
        when(requiredSearch.tags(tags)).thenReturn(requiredSearch);

        micrometerMetricsCounters.incrementEventsIgnoredCount(source, component);

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldIncrementEventsFailed() {
        final String source = "some-source";
        final String component = "some-component";

        final Counter eventsReceivedCounter = mock(Counter.class);
        final RequiredSearch requiredSearch = mock(RequiredSearch.class);
        final List<Tag> tags = List.of(mock(Tag.class), mock(Tag.class));

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(compositeMeterRegistry.get(EVENTS_FAILED_COUNTER_NAME)).thenReturn(requiredSearch);
        when(requiredSearch.counter()).thenReturn(eventsReceivedCounter);
        when(counterTagFactory.getCounterTags(source, component)).thenReturn(tags);
        when(requiredSearch.tags(tags)).thenReturn(requiredSearch);

        micrometerMetricsCounters.incrementEventsFailedCount(source, component);

        verify(eventsReceivedCounter).increment();
    }

    @Test
    public void shouldNotCountAnythingIfMetricsDisabled() throws Exception {
        final String source = "some-source";
        final String component = "some-component";

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(false);

        micrometerMetricsCounters.incrementEventsFailedCount(source, component);
        micrometerMetricsCounters.incrementEventsProcessedCount(source, component);
        micrometerMetricsCounters.incrementEventsSucceededCount(source, component);
        micrometerMetricsCounters.incrementEventsIgnoredCount(source, component);
        micrometerMetricsCounters.incrementEventsReceivedCount(source, component);

        verifyNoInteractions(compositeMeterRegistry);
    }
}
