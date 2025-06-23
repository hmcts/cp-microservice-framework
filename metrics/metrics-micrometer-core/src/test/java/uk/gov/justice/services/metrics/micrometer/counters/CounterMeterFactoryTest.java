package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.config.SourceComponentPair;
import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import java.util.Collections;
import java.util.List;

import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CounterMeterFactoryTest {

    @InjectMocks
    private CounterMeterFactory counterMeterFactory;

    @Test
    public void shouldCreateAllCounterMetersForSourceAndComponents() {
        final String source = "test-source";
        final String component = "test-component";
        final List<SourceComponentPair> sourceComponentPairs = List.of(
                new SourceComponentPair(source, component)
        );

        // run
        final List<MetricsMeter> metricsMeterList = counterMeterFactory.createAllCounterMetersForSourceAndComponents(sourceComponentPairs);

        // verify
        assertThat(metricsMeterList.size(), is(5));
        
        assertTrue(containsMeterWithName(metricsMeterList, EVENTS_FAILED_COUNTER_NAME));
        assertTrue(containsMeterWithName(metricsMeterList, EVENTS_IGNORED_COUNTER_NAME));
        assertTrue(containsMeterWithName(metricsMeterList, EVENTS_PROCESSED_COUNTER_NAME));
        assertTrue(containsMeterWithName(metricsMeterList, EVENTS_RECEIVED_COUNTER_NAME));
        assertTrue(containsMeterWithName(metricsMeterList, EVENTS_SUCCEEDED_COUNTER_NAME));
    }

    @Test
    public void shouldCreateAllCounterMetersForMultipleSourceAndComponents() {
        final String source1 = "test-source-1";
        final String component1 = "test-component-1";
        final String source2 = "test-source-2";
        final String component2 = "test-component-2";
        final List<SourceComponentPair> sourceComponentPairs = List.of(
                new SourceComponentPair(source1, component1),
                new SourceComponentPair(source2, component2)
        );

        // run
        final List<MetricsMeter> metricsMeterList = counterMeterFactory.createAllCounterMetersForSourceAndComponents(sourceComponentPairs);

        // verify
        assertThat(metricsMeterList.size(), is(10)); // 5 meters for each source-component pair
    }

    @Test
    public void shouldReturnEmptyListWhenNoSourceComponentPairsProvided() {
        final List<SourceComponentPair> sourceComponentPairs = Collections.emptyList();

        // When
        final List<MetricsMeter> metricsMeterList = counterMeterFactory.createAllCounterMetersForSourceAndComponents(sourceComponentPairs);

        // Then
        assertThat(metricsMeterList.size(), is(0));
    }

    private boolean containsMeterWithName(List<MetricsMeter> metricsMeterList, String meterName) {
        return metricsMeterList.stream()
                .anyMatch(meter -> meterName.equals(meter.metricName()));
    }
}
