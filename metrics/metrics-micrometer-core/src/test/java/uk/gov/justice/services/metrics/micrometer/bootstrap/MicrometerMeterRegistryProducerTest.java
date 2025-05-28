package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;

import javax.inject.Inject;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MicrometerMeterRegistryProducerTest {

    @Mock
    private AzureMonitorMeterRegistry azureMonitorMeterRegistry;

    @Mock
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @InjectMocks
    private MicrometerMeterRegistryProducer micrometerMeterRegistryProducer;

    @Test
    public void shouldCreateCompositeRegistryFromBothAzureAndPrometheusRegistries() throws Exception {

        final CompositeMeterRegistry compositeMeterRegistry = micrometerMeterRegistryProducer.compositeMeterRegistry();

        final Set<MeterRegistry> registries = compositeMeterRegistry.getRegistries();

        assertThat(registries.size(), is(2));
        assertThat(registries, hasItem(azureMonitorMeterRegistry));
        assertThat(registries, hasItem(prometheusMeterRegistry));
    }

    @Test
    public void shouldOnlyEverCreateSingleInstance() throws Exception {

        final CompositeMeterRegistry compositeMeterRegistry = micrometerMeterRegistryProducer.compositeMeterRegistry();
        assertThat(micrometerMeterRegistryProducer.compositeMeterRegistry(), is(sameInstance(compositeMeterRegistry)));
    }
}