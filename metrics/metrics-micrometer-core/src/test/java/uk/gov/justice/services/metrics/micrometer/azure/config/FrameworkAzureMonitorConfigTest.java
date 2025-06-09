package uk.gov.justice.services.metrics.micrometer.azure.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FrameworkAzureMonitorConfigTest {

    @Mock
    private MetricsConfiguration metricsConfiguration;

    @InjectMocks
    private FrameworkAzureMonitorConfig frameworkAzureMonitorConfig;

    @Test
    public void shouldGetEnabledFromMetricsConfiguration() throws Exception {

        final boolean enabled = true;

        when(metricsConfiguration.micrometerMetricsEnabled()).thenReturn(enabled);
        assertThat(frameworkAzureMonitorConfig.enabled(), is(enabled));
    }

    @Test
    public void shouldGetConnectionStringFromMetricsConfiguration() throws Exception {

        final String connectionString = "the-azure-connection-string";

        when(metricsConfiguration.azureMonitorConnectionString()).thenReturn(connectionString);
        assertThat(frameworkAzureMonitorConfig.connectionString(), is(connectionString));
    }

    @Test
    public void shouldReturnNulForGetOfPropertyByName() throws Exception {

        assertThat(frameworkAzureMonitorConfig.get("do-not-care"), is(nullValue()));
    }
}