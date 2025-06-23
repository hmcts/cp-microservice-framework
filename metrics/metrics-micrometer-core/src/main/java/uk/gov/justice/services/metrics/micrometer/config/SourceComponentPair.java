package uk.gov.justice.services.metrics.micrometer.config;

import java.util.Objects;

public record SourceComponentPair(String source, String component) {
    public SourceComponentPair {
        Objects.requireNonNull(source);
        Objects.requireNonNull(component);
    }
}
