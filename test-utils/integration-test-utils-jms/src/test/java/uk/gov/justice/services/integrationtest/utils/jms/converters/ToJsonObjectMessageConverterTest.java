package uk.gov.justice.services.integrationtest.utils.jms.converters;

import org.junit.jupiter.api.Test;

import javax.json.JsonObject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ToJsonObjectMessageConverterTest {

    @Test
    void shouldReturnJsonObjectGivenJsonMessage() {
        final String message = """
                {
                    "key": "value"
                }                        
                """;
        final ToJsonObjectMessageConverter toJsonObjectMessageConverter = new ToJsonObjectMessageConverter();

        final JsonObject actualJsonObject = toJsonObjectMessageConverter.convert(message);

        assertThat(actualJsonObject.getString("key"), is("value"));
    }
}