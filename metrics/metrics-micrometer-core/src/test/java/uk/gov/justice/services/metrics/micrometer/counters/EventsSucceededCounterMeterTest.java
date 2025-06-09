package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventsSucceededCounterMeterTest {

    @InjectMocks
    private EventsSucceededCounterMeter eventsSucceededCounterMeter;

    @Test
    public void shouldReturnCorrectNameAndDescription() throws Exception {

        assertThat(eventsSucceededCounterMeter.metricName(), is(EVENTS_SUCCEEDED_COUNTER_NAME));
        assertThat(eventsSucceededCounterMeter.metricDescription(), is("Count of all events successfully handled by the Event Handlers"));
    }
}