package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventsIgnoredCounterMeterTest {

    @InjectMocks
    private EventsIgnoredCounterMeter eventsIgnoredCounterMeter;

    @Test
    public void shouldReturnCorrectNameAndDescription() throws Exception {

        assertThat(eventsIgnoredCounterMeter.metricName(), is(EVENTS_IGNORED_COUNTER_NAME));
        assertThat(eventsIgnoredCounterMeter.metricDescription(), is("Count of all events ignored by the Subscription Event Processor"));
    }
}