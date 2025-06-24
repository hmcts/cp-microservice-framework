package uk.gov.justice.services.metrics.micrometer.meters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class SourceComponentPairTest {

    @Test
    void shouldCreateSourceComponentPairWithValidSourceAndComponent() {
        final String source = "test-source";
        final String component = "test-component";

        final SourceComponentPair sourceComponentPair = new SourceComponentPair(source, component);

        assertThat(sourceComponentPair.source(), is(source));
        assertThat(sourceComponentPair.component(), is(component));
    }


}