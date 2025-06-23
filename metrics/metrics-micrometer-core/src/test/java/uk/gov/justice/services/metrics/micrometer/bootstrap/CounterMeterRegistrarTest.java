package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;
import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import java.util.List;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class CounterMeterRegistrarTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private CounterMeterRegistrar counterMeterRegistrar;

    @Test
    public void shouldRegisterCounterMeter() {

        final MeterRegistry meterRegistry = mock(MeterRegistry.class);
        final MetricsMeter counterMetricsMeter = mock(GaugeMetricsMeter.class);
        List<Tag> globalTags = List.of(Tag.of("some-tag", "some-tag-value"));

        when(counterMetricsMeter.metricName()).thenReturn("some.counter.meter");
        when(counterMetricsMeter.metricDescription()).thenReturn("some description");

        counterMeterRegistrar.registerCounterMeter(counterMetricsMeter, meterRegistry, globalTags);

        verify(logger).info("Registering Micrometer Counter 'some.counter.meter'");
        verify(counterMetricsMeter).metricDescription();
    }
}