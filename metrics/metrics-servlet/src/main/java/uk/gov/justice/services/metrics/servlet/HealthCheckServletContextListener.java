package uk.gov.justice.services.metrics.servlet;


import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.metrics.servlets.HealthCheckServlet;
import jakarta.servlet.annotation.WebListener;

/**
 * Annotated context listener for wiring up the health check servlet.
 */
@WebListener
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

    public static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = new HealthCheckRegistry();

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }

}