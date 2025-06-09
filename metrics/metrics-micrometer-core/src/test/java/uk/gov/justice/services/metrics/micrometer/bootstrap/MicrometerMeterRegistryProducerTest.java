package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MicrometerMeterRegistryProducerTest {

    @Mock
    private AzureMonitorMeterRegistryFactory azureMonitorMeterRegistryFactory;

    @Mock
    private MetricsConfiguration metricsConfiguration;

    @Mock
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @Mock
    private CompositeMeterRegistryFactory compositeMeterRegistryFactory;

    @InjectMocks
    private MicrometerMeterRegistryProducer micrometerMeterRegistryProducer;

    @Test
    public void shouldCreateCompositeRegistryFromBothAzureAndPrometheusRegistriesIfMetricsAreEnabled() throws Exception {

        final AzureMonitorMeterRegistry azureMonitorMeterRegistry = mock(AzureMonitorMeterRegistry.class);
        final CompositeMeterRegistry compositeMeterRegistry = mock(CompositeMeterRegistry.class);

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(azureMonitorMeterRegistryFactory.create()).thenReturn(azureMonitorMeterRegistry);
        when(compositeMeterRegistryFactory.createNew()).thenReturn(compositeMeterRegistry);

        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(compositeMeterRegistry));

        verify(compositeMeterRegistry).add(prometheusMeterRegistry);
        verify(compositeMeterRegistry).add(azureMonitorMeterRegistry);
    }

    @Test
    public void shouldOnlyEverCreateSingleInstance() throws Exception {

        final AzureMonitorMeterRegistry azureMonitorMeterRegistry = mock(AzureMonitorMeterRegistry.class);
        final CompositeMeterRegistry compositeMeterRegistry = mock(CompositeMeterRegistry.class);

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(true);
        when(azureMonitorMeterRegistryFactory.create()).thenReturn(azureMonitorMeterRegistry);
        when(compositeMeterRegistryFactory.createNew()).thenReturn(compositeMeterRegistry);

        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));
        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));
        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));
        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));
        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));

        verify(compositeMeterRegistry, times(1)).add(prometheusMeterRegistry);
        verify(compositeMeterRegistry, times(1)).add(azureMonitorMeterRegistry);
    }

    @Test
    public void shouldCreateCompositeRegistryButNotAddAzureAndPrometheusRegistriesIfMetricsAreDisabled() throws Exception {

        final AzureMonitorMeterRegistry azureMonitorMeterRegistry = mock(AzureMonitorMeterRegistry.class);
        final CompositeMeterRegistry compositeMeterRegistry = mock(CompositeMeterRegistry.class);

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(false);
        when(compositeMeterRegistryFactory.createNew()).thenReturn(compositeMeterRegistry);

        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(compositeMeterRegistry));

        verify(compositeMeterRegistry, never()).add(prometheusMeterRegistry);
        verify(compositeMeterRegistry, never()).add(azureMonitorMeterRegistry);
    }
}