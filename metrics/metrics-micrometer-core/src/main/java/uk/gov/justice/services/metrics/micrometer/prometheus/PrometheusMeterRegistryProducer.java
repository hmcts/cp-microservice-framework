package uk.gov.justice.services.metrics.micrometer.prometheus;

import static io.micrometer.prometheusmetrics.PrometheusConfig.DEFAULT;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

@Singleton
public class PrometheusMeterRegistryProducer  {

    private final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(DEFAULT);

    @Produces
    public PrometheusMeterRegistry createMeterRegistry() {
        return prometheusMeterRegistry;
    }
}