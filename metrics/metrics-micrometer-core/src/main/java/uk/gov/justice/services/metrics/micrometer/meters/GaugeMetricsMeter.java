package uk.gov.justice.services.metrics.micrometer.meters;

import java.util.function.Supplier;

public interface GaugeMetricsMeter extends Supplier<Integer>, MetricsMeter {
}
