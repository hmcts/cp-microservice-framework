package uk.gov.justice.services.metrics.micrometer.config;

import static uk.gov.justice.services.metrics.micrometer.config.TagNames.COMPONENT_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SOURCE_TAG_NAME;

import java.util.List;

import io.micrometer.core.instrument.Tag;
import uk.gov.justice.services.metrics.micrometer.meters.SourceComponentPair;

public class TagFactory {

    public List<Tag> getSourceComponentTags(final String source, final String component) {
        return List.of(
                Tag.of(SOURCE_TAG_NAME.getTagName(), source),
                Tag.of(COMPONENT_TAG_NAME.getTagName(), component)
        );
    }

    public List<Tag> getSourceComponentTags(SourceComponentPair sourceComponentPair) {
        return getSourceComponentTags(sourceComponentPair.source(), sourceComponentPair.component());
    }
}
