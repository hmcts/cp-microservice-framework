package uk.gov.justice.services.metrics.servlet;

import io.dropwizard.metrics.servlets.MetricsServlet;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebListener;

import com.codahale.metrics.MetricRegistry;

/**
 * Annotated context listener for wiring up the metrics servlet.
 */
@WebListener
public class MetricsServletContextListener extends MetricsServlet.ContextListener {

    @Inject
    MetricRegistry metricRegistry;

    @Override
    protected MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

}