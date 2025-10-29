package uk.gov.justice.services.messaging.spi;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.messaging.JsonMetadata;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonString;
import static uk.gov.justice.services.messaging.JsonObjects.getLong;
import static uk.gov.justice.services.messaging.JsonObjects.getString;
import static uk.gov.justice.services.messaging.JsonObjects.getUUID;
import static uk.gov.justice.services.messaging.JsonObjects.getUUIDs;

/**
 * Implementation of metadata that uses a JsonObject internally to store the metadata.
 */
public class DefaultJsonMetadata extends JsonMetadata {

    private final JsonObject metadata;

    private DefaultJsonMetadata(final JsonObject metadata) {
        this.metadata = metadata;
    }

    /**
     * Construct and return an implementation of {@link MetadataBuilder}
     *
     * @return an instance of MetadataBuilder
     */
    public static MetadataBuilder metadataBuilder() {
        return new DefaultJsonMetadata.Builder();
    }

    /**
     * Construct and return an implementation of {@link MetadataBuilder} from the given {@link
     * Metadata}
     *
     * @param metadata the Metadata to add to the MetadataBuilder
     * @return an instance of MetadataBuilder
     */
    public static MetadataBuilder metadataBuilderFrom(final Metadata metadata) {
        return new DefaultJsonMetadata.Builder(metadata);
    }

    /**
     * Construct and return an implementation of {@link MetadataBuilder} from the given {@link
     * JsonObject}
     *
     * @param jsonObject the JsonObject to add to the MetadataBuilder
     * @return an instance of MetadataBuilder
     */
    public static MetadataBuilder metadataBuilderFrom(final JsonObject jsonObject) {
        return new DefaultJsonMetadata.Builder(jsonObject);
    }

    private static Metadata metadataFrom(final JsonObject jsonObject) {

        JsonString id = getJsonString(jsonObject, ID)
                .orElseThrow(() -> new IllegalArgumentException("Missing id field"));
        UUID.fromString(id.getString());

        final Optional<JsonString> jsonString = getJsonString(jsonObject, NAME);
        JsonString name = jsonString
                .orElseThrow(() -> new IllegalArgumentException("Missing name field"));
        if (name.getString().isEmpty()) {
            throw new IllegalArgumentException("Name field cannot be empty");
        }

        return new DefaultJsonMetadata(jsonObject);
    }

    @Override
    public UUID id() {
        return getUUID(metadata, ID)
                .orElseThrow(() -> new IllegalStateException("Missing id field"));
    }

    @Override
    public String name() {
        return getString(metadata, NAME)
                .orElseThrow(() -> new IllegalStateException("Missing name field"));
    }

    @Override
    public Optional<String> clientCorrelationId() {
        return getString(metadata, CLIENT_CORRELATION_PATH);
    }

    @Override
    public List<UUID> causation() {
        return getUUIDs(metadata, CAUSATION);
    }

    @Override
    public Optional<String> userId() {
        return getString(metadata, USER_ID_PATH);
    }

    @Override
    public Optional<String> sessionId() {
        return getString(metadata, SESSION_ID_PATH);
    }

    @Override
    public Optional<UUID> streamId() {
        return getUUID(metadata, STREAM_ID_PATH);
    }

    @Override
    public Optional<Long> version() {
        return getLong(metadata, VERSION_PATH);
    }

    @Override
    public Optional<Long> position() {
        return getLong(metadata, VERSION_PATH);
    }

    @Override
    public JsonObject asJsonObject() {
        return metadata;
    }

    @Override
    public Optional<ZonedDateTime> createdAt() {
        Optional<String> zonedDateTime = getString(metadata, CREATED_AT);
        return zonedDateTime.map(ZonedDateTimes::fromString);
    }

    @Override
    public Optional<String> source() {
        return getString(metadata, SOURCE);
    }

    @Override
    public Optional<Long> eventNumber() {
        return getLong(metadata, EVENT_NUMBER_PATH);
    }

    @Override
    public Optional<Long> previousEventNumber() {
        return getLong(metadata, PREVIOUS_EVENT_NUMBER_PATH);
    }

    @Override
    @SuppressWarnings({"squid:MethodCyclomaticComplexity", "squid:S1067", "squid:S00122"})
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJsonMetadata that = (DefaultJsonMetadata) o;
        return Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata);
    }


    public static class Builder implements MetadataBuilder {

        private JsonObjectBuilderWrapper json;

        private Builder() {
            json = new JsonObjectBuilderWrapper();
        }

        private Builder(final Metadata metadata) {
            json = new JsonObjectBuilderWrapper(metadata.asJsonObject());
        }

        private Builder(final JsonObject jsonObject) {
            json = new JsonObjectBuilderWrapper(jsonObject);
        }

        @Override
        public MetadataBuilder withId(final UUID id) {
            json.add(id.toString(), ID);
            return this;
        }

        @Override
        public MetadataBuilder createdAt(final ZonedDateTime dateCreated) {
            json.add(ZonedDateTimes.toString(dateCreated), CREATED_AT);
            return this;
        }

        @Override
        public MetadataBuilder withName(final String name) {
            json.add(name, NAME);
            return this;
        }

        @Override
        public MetadataBuilder withCausation(final UUID... uuid) {
            final JsonArrayBuilder causationArray = getJsonBuilderFactory().createArrayBuilder();
            for (UUID id : uuid) {
                causationArray.add(id.toString());
            }
            json.add(causationArray, CAUSATION);
            return this;
        }

        @Override
        public MetadataBuilder withClientCorrelationId(final String clientId) {
            json.add(clientId, CLIENT_CORRELATION_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withUserId(final String userId) {
            json.add(userId, USER_ID_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withSessionId(final String sessionId) {
            json.add(sessionId, SESSION_ID_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withStreamId(final UUID streamId) {
            json.add(streamId.toString(), STREAM_ID_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withVersion(final long version) {
            json.add(BigDecimal.valueOf(version), VERSION_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withPosition(final long version) {
            json.add(BigDecimal.valueOf(version), VERSION_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withSource(final String source) {
            json.add(source, SOURCE);
            return this;
        }

        @Override
        public MetadataBuilder withEventNumber(final long eventNumber) {
            json.add(BigDecimal.valueOf(eventNumber), EVENT_NUMBER_PATH);
            return this;
        }

        @Override
        public MetadataBuilder withPreviousEventNumber(final long previousEventNumber) {
            json.add(BigDecimal.valueOf(previousEventNumber), PREVIOUS_EVENT_NUMBER_PATH);
            return this;
        }

        @Override
        public Metadata build() {
            return metadataFrom(json.build());
        }
    }
}
