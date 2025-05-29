package uk.gov.justice.services.metrics.micrometer.config;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;

import uk.gov.justice.services.common.configuration.Value;

import javax.inject.Inject;

public class MetricsConfiguration {

    private static final String AZURE_METRICS_MONITOR_CONNECTION_STRING = "azure.metrics.monitor.connection.string";
    private static final String MICROMETER_METRICS_ENABLED = "micrometer.metrics.enabled";

    @Inject
    @Value(key = MICROMETER_METRICS_ENABLED, defaultValue = "false")
    private String micrometerMetricsEnabled;

    @Inject
    @Value(key = AZURE_METRICS_MONITOR_CONNECTION_STRING, defaultValue = "azure-metrics-connection-string-not-set")
    private String azureMonitorConnectionString;

    public boolean micrometerMetricsEnabled() {
        return parseBoolean(micrometerMetricsEnabled);
    }

    public String azureMonitorConnectionString() {

        if(azureMonitorConnectionString == null) {
            throw new MicometerMetricsConfigurationException(format("Azure monitor connection string found in JNDI. Value should be set for the name '%s'", AZURE_METRICS_MONITOR_CONNECTION_STRING));
        }

        return azureMonitorConnectionString;
    }
}
