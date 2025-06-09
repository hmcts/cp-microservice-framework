package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventsProcessedCounterMeterTest {

    @InjectMocks
    private EventsProcessedCounterMeter eventsProcessedCounterMeter;

    @Test
    public void shouldReturnCorrectNameAndDescription() throws Exception {

        assertThat(eventsProcessedCounterMeter.metricName(), is(EVENTS_PROCESSED_COUNTER_NAME));
        assertThat(eventsProcessedCounterMeter.metricDescription(), is("Count of all events passed to the Subscription Event Processor"));
    }
}