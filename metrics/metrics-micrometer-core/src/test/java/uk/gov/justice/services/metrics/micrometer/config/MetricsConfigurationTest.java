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
    public void shouldGetMetricsEnabledAsBoolean() {

        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(false));

        setField(metricsConfiguration, "micrometerMetricsEnabled", "true");
        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(true));

        setField(metricsConfiguration, "micrometerMetricsEnabled", "false");
        assertThat(metricsConfiguration.micrometerMetricsEnabled(), is(false));
    }

    @Test
    public void shouldGetAzureMonitorConnectionString() {

        final String azureConnectionString = "azure-connection-string";

        setField(metricsConfiguration, "azureMonitorConnectionString", azureConnectionString);

        assertThat(metricsConfiguration.azureMonitorConnectionString(), is(azureConnectionString));
    }

    @Test
    public void shouldGetStatisticTimerIntervalMilliseconds() {

        setField(metricsConfiguration, "statisticTimerIntervalMilliseconds", "30000");
        assertThat(metricsConfiguration.statisticTimerIntervalMilliseconds(), is(30000L));

        setField(metricsConfiguration, "statisticTimerIntervalMilliseconds", "60000");
        assertThat(metricsConfiguration.statisticTimerIntervalMilliseconds(), is(60000L));
    }

    @Test
    public void shouldGetStatisticTimerDelayMilliseconds() {

        setField(metricsConfiguration, "statisticTimerDelayMilliseconds", "5000");
        assertThat(metricsConfiguration.statisticTimerDelayMilliseconds(), is(5000L));

        setField(metricsConfiguration, "statisticTimerDelayMilliseconds", "10000");
        assertThat(metricsConfiguration.statisticTimerDelayMilliseconds(), is(10000L));
    }
}
