package uk.gov.justice.services.metrics.micrometer.meters;

public interface MetricsMeter {

    String metricName();

    String metricDescription();

    SourceComponentPair sourceComponentPair();
}
