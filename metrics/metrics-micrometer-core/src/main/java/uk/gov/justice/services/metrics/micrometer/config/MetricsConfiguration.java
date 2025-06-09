package uk.gov.justice.services.metrics.micrometer.config;

import static java.lang.Boolean.parseBoolean;

import uk.gov.justice.services.common.configuration.Value;

import javax.inject.Inject;

public class MetricsConfiguration {

    @Inject
    @Value(key = "micrometer.metrics.enabled", defaultValue = "false")
    private String micrometerMetricsEnabled;

    @Inject
    @Value(key = "azure.metrics.monitor.connection.string", defaultValue = "azure-metrics-connection-string-not-set")
    private String azureMonitorConnectionString;

    public boolean micrometerMetricsEnabled() {
        return parseBoolean(micrometerMetricsEnabled);
    }

    public String azureMonitorConnectionString() {
        return azureMonitorConnectionString;
    }
}
