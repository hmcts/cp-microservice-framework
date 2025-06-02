package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventsReceivedCounterMeterTest {

    @InjectMocks
    private EventsReceivedCounterMeter eventsReceivedCounterMeter;

    @Test
    public void shouldReturnCorrectNameAndDescription() throws Exception {

        assertThat(eventsReceivedCounterMeter.metricName(), is(EVENTS_RECEIVED_COUNTER_NAME));
        assertThat(eventsReceivedCounterMeter.metricDescription(), is("Count of events received via the Event Topic"));
    }
}