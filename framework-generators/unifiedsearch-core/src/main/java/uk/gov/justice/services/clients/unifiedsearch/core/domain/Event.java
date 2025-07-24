package uk.gov.justice.services.clients.unifiedsearch.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
    private final String name;
    private final String transformerConfig;
    private final String indexName;

    @JsonCreator
    public Event(
            @JsonProperty("name") final String name,
            @JsonProperty("transformer_config") final String transformerConfig,
            @JsonProperty("index_name") final String indexName) {
        this.name = name;
        this.transformerConfig = transformerConfig;
        this.indexName = indexName;
    }


    public String getName() {
        return name;
    }

    public String getTransformerConfig() {
        return transformerConfig;
    }

    public String getIndexName() {
        return indexName;
    }

}
