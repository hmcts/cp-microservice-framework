package uk.gov.justice.services.core.json;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.UUID;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.services.messaging.JsonEnvelope.METADATA;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataBuilder;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
public class PayloadExtractorTest {

    @InjectMocks
    private PayloadExtractor payloadExtractor;

    @Test
    public void shouldRemoveTheMetadataFromAnEnvelopeJsonStringAndConvertToJsonObject() throws Exception {

        final UUID streamId = randomUUID();
        final String commandName = "time-travel-initiated";

        final JsonEnvelope jsonEnvelope = envelopeFrom(metadataBuilder()
                        .withId(randomUUID())
                        .withStreamId(streamId)
                        .withName(commandName),
                getJsonBuilderFactory().createObjectBuilder()
                        .add("destination", "Jurassic Era"));

        final String envelopeJson = jsonEnvelope.toDebugStringPrettyPrint();

        final JSONObject jsonObject = payloadExtractor.extractPayloadFrom(envelopeJson);

        final String json = jsonObject.toString();

        with(json)
                .assertThat("$.destination", is("Jurassic Era"))
                .assertNotDefined(METADATA)
        ;
    }
}
