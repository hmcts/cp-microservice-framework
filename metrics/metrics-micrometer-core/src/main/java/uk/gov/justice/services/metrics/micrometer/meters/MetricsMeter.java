package uk.gov.justice.services.metrics.micrometer.meters;

import static java.util.Collections.emptyList;

import java.util.List;

import io.micrometer.core.instrument.Tag;

public interface MetricsMeter {

    String metricName();
    String metricDescription();
}
