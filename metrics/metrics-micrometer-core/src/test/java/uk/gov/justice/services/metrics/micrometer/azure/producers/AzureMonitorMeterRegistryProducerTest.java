package uk.gov.justice.services.metrics.micrometer.azure.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.metrics.micrometer.azure.config.FrameworkAzureMonitorConfig;
import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;
import uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil;

import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AzureMonitorMeterRegistryProducerTest {

    @Spy
    private FrameworkAzureMonitorConfig frameworkAzureMonitorConfig = new FrameworkAzureMonitorConfig();

    @InjectMocks
    private AzureMonitorMeterRegistryProducer azureMonitorMeterRegistryProducer;

    @Test
    public void shouldCreateAzureMonitorMeterRegistryWithTheCorrectConfiguration() throws Exception {

        final MetricsConfiguration metricsConfiguration = mock(MetricsConfiguration.class);

        setField(frameworkAzureMonitorConfig, "metricsConfiguration", metricsConfiguration);
        when(metricsConfiguration.azureMonitorConnectionString()).thenReturn("InstrumentationKey=something;IngestionEndpoint=https://somewhere.com/;LiveEndpoint=https://somewhere-else.com/;ApplicationId=something-else");

        assertThat(azureMonitorMeterRegistryProducer.meterRegistry(), is(notNullValue()));
    }
}