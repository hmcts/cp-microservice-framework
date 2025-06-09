package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;
import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;

import io.micrometer.core.instrument.MeterRegistry;
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
    public void shouldRegisterCounterMeter() throws Exception {

        final MeterRegistry meterRegistry = mock(MeterRegistry.class);
        final MetricsMeter counterMetricsMeter = mock(GaugeMetricsMeter.class);

        when(counterMetricsMeter.metricName()).thenReturn("some.counter.meter");

        counterMeterRegistrar.registerCounterMeter(counterMetricsMeter, meterRegistry);

        verify(logger).info("Registering Micrometer Counter 'some.counter.meter'");
        verify(counterMetricsMeter).metricDescription();
        verify(counterMetricsMeter).metricTags();
    }
}