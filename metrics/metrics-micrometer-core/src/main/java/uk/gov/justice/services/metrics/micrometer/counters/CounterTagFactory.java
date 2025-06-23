package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.config.TagNames.COMPONENT_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.ENV_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SERVICE_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SOURCE_TAG_NAME;

import uk.gov.justice.services.common.configuration.ContextNameProvider;
import uk.gov.justice.services.metrics.micrometer.config.MetricsConfiguration;

import java.util.List;

import javax.inject.Inject;

import io.micrometer.core.instrument.Tag;

public class CounterTagFactory {

    @Inject
    private MetricsConfiguration metricsConfiguration;

    @Inject
    private ContextNameProvider contextNameProvider;

    public List<Tag> getCounterTags(final String source, final String component) {

        return List.of(
                Tag.of(SOURCE_TAG_NAME.getTagName(), source),
                Tag.of(COMPONENT_TAG_NAME.getTagName(), component),
                Tag.of(ENV_TAG_NAME.getTagName(), metricsConfiguration.micrometerEnv()),
                Tag.of(SERVICE_TAG_NAME.getTagName(), contextNameProvider.getContextName())
        );
    }
}
