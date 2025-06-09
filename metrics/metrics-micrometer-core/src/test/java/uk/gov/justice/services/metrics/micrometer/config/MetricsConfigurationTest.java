package uk.gov.justice.services.metrics.micrometer.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
}