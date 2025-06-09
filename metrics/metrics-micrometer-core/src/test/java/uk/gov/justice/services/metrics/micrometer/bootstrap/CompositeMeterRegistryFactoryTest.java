package uk.gov.justice.services.metrics.micrometer.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompositeMeterRegistryFactoryTest {

    @InjectMocks
    private CompositeMeterRegistryFactory compositeMeterRegistryFactory;

    @Test
    public void shouldCreateNewCompositeRegistry() throws Exception {

        final CompositeMeterRegistry compositeMeterRegistry = compositeMeterRegistryFactory.createNew();

        assertThat(compositeMeterRegistry, is(notNullValue()));
    }
}