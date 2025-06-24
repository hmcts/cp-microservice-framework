package uk.gov.justice.services.metrics.micrometer.bootstrap;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import uk.gov.justice.services.metrics.micrometer.config.TagFactory;
import uk.gov.justice.services.metrics.micrometer.meters.GaugeMetricsMeter;
import uk.gov.justice.services.metrics.micrometer.meters.SourceComponentPair;

import java.util.List;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GaugeMeterRegistrarTest {

    @Mock
    private Logger logger;

    @Mock
    private TagFactory tagFactory;

    @InjectMocks
    private GaugeMeterRegistrar gaugeMeterRegistrar;

    private MockedStatic<Gauge> gaugeStatic;

    @BeforeEach
    public void setUp() {
        gaugeStatic = mockStatic(Gauge.class);
    }

    @AfterEach
    public void tearDown() {
        gaugeStatic.close();
    }

    @Test
    public void shouldRegisterGaugeMeter() {

        final MeterRegistry meterRegistry = mock(MeterRegistry.class);
        final GaugeMetricsMeter gaugeMetricsMeter = mock(GaugeMetricsMeter.class);
        final SourceComponentPair sourceComponentPair = mock(SourceComponentPair.class);
        final List<Tag> tags = List.of(mock(Tag.class));
        final Gauge.Builder<Integer> gaugeBuilder = mock(Gauge.Builder.class);
        final Gauge gauge = mock(Gauge.class);

        when(gaugeMetricsMeter.metricName()).thenReturn("meterName");
        when(gaugeMetricsMeter.metricDescription()).thenReturn("some description");
        when(gaugeMetricsMeter.sourceComponentPair()).thenReturn(sourceComponentPair);
        when(tagFactory.getSourceComponentTags(sourceComponentPair)).thenReturn(tags);
        when(gaugeBuilder.tags(tags)).thenReturn(gaugeBuilder);
        when(gaugeBuilder.description("some description")).thenReturn(gaugeBuilder);
        when(gaugeBuilder.register(meterRegistry)).thenReturn(gauge);

        gaugeStatic.when(() -> Gauge.builder("meterName", gaugeMetricsMeter)).thenReturn(gaugeBuilder);

        // run
        gaugeMeterRegistrar.registerGaugeMeter(gaugeMetricsMeter, meterRegistry);

        // verify
        verify(logger).info("Registering Micrometer Gauge 'meterName'");
        verify(gaugeMetricsMeter, atLeastOnce()).metricName();
        verify(gaugeMetricsMeter).metricDescription();
        verify(gaugeMetricsMeter).sourceComponentPair();
        verify(tagFactory).getSourceComponentTags(sourceComponentPair);
        gaugeStatic.verify(() -> Gauge.builder("meterName",gaugeMetricsMeter));
        verify(gaugeBuilder).tags(tags);
        verify(gaugeBuilder).description("some description");
        verify(gaugeBuilder).register(meterRegistry);

    }
}
