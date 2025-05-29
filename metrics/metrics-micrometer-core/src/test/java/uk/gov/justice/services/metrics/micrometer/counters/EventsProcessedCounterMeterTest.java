package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.config.MetricsMeterNames;

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