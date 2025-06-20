package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;

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
public class GaugeMeterRegistrarTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private GaugeMeterRegistrar gaugeMeterRegistrar;

    @Test
    public void shouldRegisterGaugeMeter() throws Exception {

        final MeterRegistry meterRegistry = mock(MeterRegistry.class);
        final GaugeMetricsMeter gaugeMetricsMeter = mock(GaugeMetricsMeter.class);

        when(gaugeMetricsMeter.metricName()).thenReturn("some.gauge.meter");

        gaugeMeterRegistrar.registerGaugeMeter(gaugeMetricsMeter, meterRegistry, List.of(Tag.of("some-tag", "some-tag-value")) );

        verify(logger).info("Registering Micrometer Gauge 'some.gauge.meter'");
        verify(gaugeMetricsMeter).metricDescription();
        verify(gaugeMetricsMeter).metricTags();
    }
}
