package uk.gov.justice.services.clients.unifiedsearch.core.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnifiedSearchDescriptor {

    private final String name;
    private final String specVersion;
    private final String service;
    private final String serviceComponent;
    private final List<Event> events;

    @JsonCreator
    public UnifiedSearchDescriptor(@JsonProperty("name") final String name,
                                   @JsonProperty("spec_version") final String specVersion,
                                   @JsonProperty("service") final String service,
                                   @JsonProperty("service_component") final String serviceComponent,
                                   @JsonProperty("event") final List<Event> events) {
        this.name = name;
        this.specVersion = specVersion;
        this.service = service;
        this.serviceComponent = serviceComponent;
        this.events = events;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public String getService() {
        return service;
    }

    public String getServiceComponent() {
        return serviceComponent;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getName() {
        return name;
    }

}
