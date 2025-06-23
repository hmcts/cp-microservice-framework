package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.COMPONENT_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.ENV_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SERVICE_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SOURCE_TAG_NAME;

import uk.gov.justice.services.common.configuration.ContextNameProvider;
import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import java.util.List;

import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CounterTagFactoryTest {

    @Mock
    private MetricsConfiguration metricsConfiguration;

    @Mock
    private ContextNameProvider contextNameProvider;

    @InjectMocks
    private CounterTagFactory counterTagFactory;

    @Test
    public void shouldName() throws Exception {

        final String source = "some-source";
        final String component = "some-component";
        final String serviceName = "some-service";
        final String environmentName = "some-server-somewhere";

        when(metricsConfiguration.micrometerEnv()).thenReturn(environmentName);
        when(contextNameProvider.getContextName()).thenReturn(serviceName);

        final List<Tag> counterTags = counterTagFactory.getCounterTags(source, component);

        assertThat(counterTags.size(), is(4));

        assertThat(counterTags, hasItem(Tag.of(SOURCE_TAG_NAME.getTagName(), source)));
        assertThat(counterTags, hasItem(Tag.of(COMPONENT_TAG_NAME.getTagName(), component)));
        assertThat(counterTags, hasItem(Tag.of(SERVICE_TAG_NAME.getTagName(), serviceName)));
        assertThat(counterTags, hasItem(Tag.of(ENV_TAG_NAME.getTagName(), environmentName)));
    }
}