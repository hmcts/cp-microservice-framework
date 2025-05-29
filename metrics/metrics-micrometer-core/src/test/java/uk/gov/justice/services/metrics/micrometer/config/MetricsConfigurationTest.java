package uk.gov.justice.services.metrics.micrometer.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MetricsConfigurationTest {

    @InjectMocks
    private MetricsConfiguration metricsConfiguration;

    @Test
    public void shouldGetMetricsEnabledAsBoolean() throws Exception {

        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(false));

        setField(metricsConfiguration, "micrometerMetricsEnabled", "true");
        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(true));

        setField(metricsConfiguration, "micrometerMetricsEnabled", "false");
        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(false));
    }

    @Test
    public void shouldGetAzureMonitorConnectionString() throws Exception {

        final String azureConnectionString = "azure-connection-string";

        setField(metricsConfiguration, "azureMonitorConnectionString", azureConnectionString);

        assertThat(metricsConfiguration.azureMonitorConnectionString(), is(azureConnectionString));
    }

    @Test
    public void shouldThrowMicometerMetricsConfigurationExceptionIfNoAzureConnectionStringFound() throws Exception {

        setField(metricsConfiguration, "azureMonitorConnectionString", null);

        final MicometerMetricsConfigurationException micometerMetricsConfigurationException = assertThrows(
                MicometerMetricsConfigurationException.class,
                () -> metricsConfiguration.azureMonitorConnectionString());

        assertThat(micometerMetricsConfigurationException.getMessage(), is("Azure monitor connection string found in JNDI. Value should be set for the name 'azure.metrics.monitor.connection.string'"));
    }
}