package uk.gov.justice.services.metrics.micrometer.meters;

public interface GaugeMetricsMeter extends MetricsMeter {

    int measure();
}
