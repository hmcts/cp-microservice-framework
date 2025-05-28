package uk.gov.justice.services.metrics.micrometer.registry;

import static org.mockito.Mockito.mockStatic;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MetricsRegistrarTest {

    @InjectMocks
    private MetricsRegistrar metricsRegistrar;

    @Test
    public void shouldRegisterMetricsRegistry() throws Exception {

        final SimpleMeterRegistry simpleMeterRegistry = new SimpleMeterRegistry();

        try (final MockedStatic<Metrics> metricsMockedStatic = mockStatic(Metrics.class)) {
            metricsRegistrar.addRegistry(simpleMeterRegistry);

            metricsMockedStatic.verify(() -> Metrics.addRegistry(simpleMeterRegistry));
        }
    }
}