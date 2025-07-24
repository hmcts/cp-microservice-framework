package uk.gov.justice.subscription.domain.eventsource;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    private final String jmsUri;
    private final String restUri;
    private final String dataSource;

    @JsonCreator
    public Location(@JsonProperty("jms_uri") final String jmsUri,
                    @JsonProperty("rest_uri") final String restUri,
                    @JsonProperty("data_source") final String dataSource) {
        this.jmsUri = jmsUri;
        this.restUri = restUri;
        this.dataSource = dataSource;
    }

    public String getJmsUri() {
        return jmsUri;
    }

    public Optional<String> getRestUri() {
        return ofNullable(restUri);
    }

    public Optional<String> getDataSource() {
        return ofNullable(dataSource);
    }
}
