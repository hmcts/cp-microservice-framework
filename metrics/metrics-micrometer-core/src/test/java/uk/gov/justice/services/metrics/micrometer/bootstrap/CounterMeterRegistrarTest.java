package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import uk.gov.justice.services.metrics.micrometer.config.TagFactory;
import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;
import uk.gov.justice.services.metrics.micrometer.meters.SourceComponentPair;

import java.util.List;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class CounterMeterRegistrarTest {

    @Mock
    private Logger logger;

    @Mock
    private TagFactory tagFactory;

    private MockedStatic<Counter> counterStatic;

    @BeforeEach
    public void setUp() {
        counterStatic = mockStatic(Counter.class);
    }

    @AfterEach
    public void tearDown() {
        counterStatic.close();
    }

    @InjectMocks
    private CounterMeterRegistrar counterMeterRegistrar;

    @Test
    public void shouldRegisterCounterMeter() {

        final MeterRegistry meterRegistry = mock(MeterRegistry.class);
        final MetricsMeter counterMetricsMeter = mock(MetricsMeter.class);
        final SourceComponentPair sourceComponentPair = mock(SourceComponentPair.class);
        final List<Tag> tags = List.of(mock(Tag.class));
        final Counter.Builder counterBuilder = mock(Counter.Builder.class);
        final Counter counter = mock(Counter.class);

        when(counterMetricsMeter.metricName()).thenReturn("meterName");
        when(counterMetricsMeter.metricDescription()).thenReturn("some description");
        when(counterMetricsMeter.sourceComponentPair()).thenReturn(sourceComponentPair);
        when(tagFactory.getSourceComponentTags(sourceComponentPair)).thenReturn(tags);
        when(counterBuilder.tags(tags)).thenReturn(counterBuilder);
        when(counterBuilder.description("some description")).thenReturn(counterBuilder);
        when(counterBuilder.register(meterRegistry)).thenReturn(counter);

        counterStatic.when(() -> Counter.builder("meterName")).thenReturn(counterBuilder);

        // run
        counterMeterRegistrar.registerCounterMeter(counterMetricsMeter, meterRegistry);

        // verify
        verify(logger).info("Registering Micrometer Counter 'meterName'");
        verify(counterMetricsMeter, atLeastOnce()).metricName();
        verify(counterMetricsMeter).metricDescription();
        verify(counterMetricsMeter).sourceComponentPair();
        verify(tagFactory).getSourceComponentTags(sourceComponentPair);
        counterStatic.verify(() -> Counter.builder("meterName"));
        verify(counterBuilder).tags(tags);
        verify(counterBuilder).description("some description");
        verify(counterBuilder).register(meterRegistry);

    }
}