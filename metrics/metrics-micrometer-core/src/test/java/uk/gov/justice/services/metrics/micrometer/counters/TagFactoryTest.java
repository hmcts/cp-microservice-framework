package uk.gov.justice.services.metrics.micrometer.counters;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.COMPONENT_TAG_NAME;
import static uk.gov.justice.services.metrics.micrometer.config.TagNames.SOURCE_TAG_NAME;

import java.util.List;

import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import uk.gov.justice.services.metrics.micrometer.config.TagFactory;
import uk.gov.justice.services.metrics.micrometer.meters.SourceComponentPair;

public class TagFactoryTest {


    @Test
    public void shouldgetSourceComponentTagsWithSourceAndComponent() {

        final String source = "some-source";
        final String component = "some-component";

        final TagFactory tagFactory = new TagFactory();
        final List<Tag> counterTags = tagFactory.getSourceComponentTags(source, component);

        assertThat(counterTags.size(), is(2));

        assertThat(counterTags, hasItem(Tag.of(SOURCE_TAG_NAME.getTagName(), source)));
        assertThat(counterTags, hasItem(Tag.of(COMPONENT_TAG_NAME.getTagName(), component)));
    }

    @Test
    public void shouldgetSourceComponentTagsWithSourceComponentPair() {

        final String source = "some-source";
        final String component = "some-component";

        final TagFactory tagFactory = new TagFactory();
        final List<Tag> counterTags = tagFactory.getSourceComponentTags(new SourceComponentPair(source, component));

        assertThat(counterTags.size(), is(2));

        assertThat(counterTags, hasItem(Tag.of(SOURCE_TAG_NAME.getTagName(), source)));
        assertThat(counterTags, hasItem(Tag.of(COMPONENT_TAG_NAME.getTagName(), component)));
    }
}
