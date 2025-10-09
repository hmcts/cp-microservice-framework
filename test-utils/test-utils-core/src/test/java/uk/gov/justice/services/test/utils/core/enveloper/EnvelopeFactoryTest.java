package uk.gov.justice.services.test.utils.core.enveloper;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.messaging.JsonObjects.jsonBuilderFactory;
import static uk.gov.justice.services.test.utils.core.matchers.JsonEnvelopeMatcher.jsonEnvelope;
import static uk.gov.justice.services.test.utils.core.matchers.JsonEnvelopeMetadataMatcher.metadata;
import static uk.gov.justice.services.test.utils.core.matchers.JsonEnvelopePayloadMatcher.payloadIsJson;

import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class EnvelopeFactoryTest {

    private EnvelopeFactory envelopeFactory = new EnvelopeFactory();

    @Test
    public void shouldCreateAJsonEnvelope() throws Exception {

        final String commandName = "some.command-or-other";

        final JsonObject payload = jsonBuilderFactory.createObjectBuilder()
                .add("payloadName", "payloadValue")
                .build();

        final JsonEnvelope jsonEnvelope = envelopeFactory.create(commandName, payload);

        assertThat(jsonEnvelope, jsonEnvelope(
                metadata().withName(commandName),
                payloadIsJson(
                        withJsonPath("$.payloadName", equalTo("payloadValue"))
                )));
    }
}
