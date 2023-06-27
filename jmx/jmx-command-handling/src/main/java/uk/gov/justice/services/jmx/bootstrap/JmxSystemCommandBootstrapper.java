package uk.gov.justice.services.jmx.bootstrap;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;

public class JmxSystemCommandBootstrapper implements Extension {

    private final ObjectFactory objectFactory;

    public JmxSystemCommandBootstrapper() {
        this(new ObjectFactory());
    }
    
    public JmxSystemCommandBootstrapper(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void afterDeploymentValidation(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        objectFactory.systemCommandScanner().registerSystemCommands(beanManager);
    }
}
