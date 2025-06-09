package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.metrics.micrometer.azure.config.FrameworkAzureMonitorConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AzureMonitorMeterRegistryFactoryTest {

    @Mock
    private FrameworkAzureMonitorConfig frameworkAzureMonitorConfig;

    @InjectMocks
    private AzureMonitorMeterRegistryFactory  azureMonitorMeterRegistryFactory;

    @Test
    public void shouldCreateNewAzureMonitorMeterRegistry() throws Exception {

        when(frameworkAzureMonitorConfig.connectionString()).thenReturn("InstrumentationKey=some-instrumentation-key");

        assertThat(azureMonitorMeterRegistryFactory.create(), is(notNullValue()));
    }
}