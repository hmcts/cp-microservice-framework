package uk.gov.justice.subscription.domain.eventsource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventSourceDefinition {

    private final String name;
    private final boolean is_default;
    private final Location location;

    @JsonCreator
    public EventSourceDefinition(@JsonProperty("name") final String name,
                                 @JsonProperty("is_default") final boolean is_default,
                                 @JsonProperty("location") final Location location) {
        this.name = name;
        this.is_default = is_default;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isDefault() {
        return is_default;
    }
}
