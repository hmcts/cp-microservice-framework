package uk.gov.justice.services.metrics.micrometer.meters;

public class MetricsProviderException extends RuntimeException {

    public MetricsProviderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
