package uk.gov.justice.services.integrationtest.utils.jms.converters;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

public class ToJsonObjectMessageConverter implements MessageConverter<JsonObject> {

    @Override
    public JsonObject convert(String message) {
        return Json.createReader(new StringReader(message)).readObject();
    }
}
