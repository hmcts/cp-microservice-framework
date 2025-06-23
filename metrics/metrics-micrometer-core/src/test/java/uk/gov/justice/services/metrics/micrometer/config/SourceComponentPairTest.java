package uk.gov.justice.services.metrics.micrometer.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void shouldThrowNullPointerExceptionWhenSourceIsNull() {
        final String component = "test-component";

        assertThrows(NullPointerException.class, () -> new SourceComponentPair(null, component));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenComponentIsNull() {
        final String source = "test-source";

        assertThrows(NullPointerException.class, () -> new SourceComponentPair(source, null));
    }

}