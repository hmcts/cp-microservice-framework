package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;
import uk.gov.justice.services.metrics.micrometer.counters.EventsProcessedCounterMeter;
import uk.gov.justice.services.metrics.micrometer.meters.gauges.CountEventStreamsGaugeMeter;
import uk.gov.justice.services.metrics.micrometer.registry.MetricsRegistrar;

import javax.inject.Inject;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class MicrometerMetricsBootstrapperTest {

    @Mock
    private MetricsConfiguration metricsConfiguration;

    @Mock
    private CompositeMeterRegistry compositeMeterRegistry;

    @Mock
    private MetricsRegistrar metricsRegistrar;

    @Mock
    private CountEventStreamsGaugeMeter countEventStreamsGaugeMeter;

    @Mock
    private GaugeMeterRegistrar gaugeMeterRegistrar;

    @Mock
    private CounterMeterRegistrar counterMeterRegistrar;

    @Mock
    private EventsProcessedCounterMeter eventsProcessedCounterMeter;

    @Mock
    private Logger logger;

    @InjectMocks
    private MicrometerMetricsBootstrapper micrometerMetricsBootstrapper;

    @Test
    public void shouldCreateRegistriesAndGaugesIfMetricsEnabled() throws Exception {

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);

        micrometerMetricsBootstrapper.bootstrapMetrics();

        final InOrder inOrder = inOrder(
                logger,
                metricsRegistrar,
                gaugeMeterRegistrar,
                counterMeterRegistrar);

        inOrder.verify(logger).info("Starting micrometer metrics");
        inOrder.verify(metricsRegistrar).addRegistry(compositeMeterRegistry);
        inOrder.verify(gaugeMeterRegistrar).registerGaugeMeter(countEventStreamsGaugeMeter, compositeMeterRegistry);
        inOrder.verify(counterMeterRegistrar).registerCounterMeter(eventsProcessedCounterMeter, compositeMeterRegistry);
    }

    @Test
    public void shouldWarnAndDoNothingIfMetricsDisabled() throws Exception {

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(false);

        micrometerMetricsBootstrapper.bootstrapMetrics();

        verify(logger).warn("Micrometer metrics disabled");
        verify(metricsRegistrar, never()).addRegistry(compositeMeterRegistry);
        verify(gaugeMeterRegistrar, never()).registerGaugeMeter(countEventStreamsGaugeMeter, compositeMeterRegistry);
        verify(counterMeterRegistrar, never()).registerCounterMeter(eventsProcessedCounterMeter, compositeMeterRegistry);
    }
}