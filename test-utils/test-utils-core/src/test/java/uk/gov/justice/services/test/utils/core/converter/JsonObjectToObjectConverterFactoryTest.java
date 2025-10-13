package uk.gov.justice.services.test.utils.core.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static uk.gov.justice.services.messaging.JsonObjects.jsonBuilderFactory;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class JsonObjectToObjectConverterFactoryTest {

    @Test
    public void testUsableJsonObjectToObjectConverterIsProduced(){
        JsonObjectToObjectConverter converter = JsonObjectToObjectConverterFactory.createJsonObjectToObjectConverter();

        assertThat(converter, is(notNullValue()));

        JsonObject jsonObject = jsonBuilderFactory.createObjectBuilder()
                .add("testAttribute", "testValue")
                .build();

        TestPojo convertedPojo = converter.convert(jsonObject, TestPojo.class);
        assertThat(convertedPojo.getTestAttribute(), is("testValue"));
    }

    private static class TestPojo {

        private String testAttribute;

        public String getTestAttribute() {
            return testAttribute;
        }

        public void setTestAttribute(String testAttribute) {
            this.testAttribute = testAttribute;
        }

    }
}