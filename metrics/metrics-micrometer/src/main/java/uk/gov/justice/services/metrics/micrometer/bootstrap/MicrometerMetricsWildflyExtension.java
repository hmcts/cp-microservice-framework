package uk.gov.justice.services.metrics.micrometer.bootstrap;

import uk.gov.justice.services.framework.utilities.cdi.CdiInstanceResolver;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import com.google.common.annotations.VisibleForTesting;

public class MicrometerMetricsWildflyExtension implements Extension {

    private final CdiInstanceResolver cdiInstanceResolver;

    public MicrometerMetricsWildflyExtension()  {
        this.cdiInstanceResolver = new CdiInstanceResolver();
    }

    @VisibleForTesting
    public MicrometerMetricsWildflyExtension(final CdiInstanceResolver cdiInstanceResolver) {
        this.cdiInstanceResolver = cdiInstanceResolver;
    }

    public void afterDeploymentValidation(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {

        final MicrometerMetricsBootstrapper micrometerMetricsBootstrapper = cdiInstanceResolver.getInstanceOf(
                MicrometerMetricsBootstrapper.class,
                beanManager);

        micrometerMetricsBootstrapper.bootstrapMetrics();

    }
}
