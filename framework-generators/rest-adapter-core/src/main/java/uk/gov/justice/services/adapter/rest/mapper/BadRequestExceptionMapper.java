package uk.gov.justice.services.adapter.rest.mapper;

import static jakarta.json.Json.createObjectBuilder;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.status;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.justice.services.core.json.JsonSchemaValidationException;
import uk.gov.justice.services.core.json.JsonValidationLoggerHelper;

import jakarta.inject.Inject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Inject
    Logger logger;

    @Inject
    JsonValidationLoggerHelper jsonValidationLoggerHelper;

    @Override
    public Response toResponse(final BadRequestException exception) {
        logger.debug("Bad Request", exception);
        final JsonObjectBuilder builder = createObjectBuilder().add("error", exception.getMessage());
        if (exception.getCause() instanceof JsonSchemaValidationException) {
            builder.add("validationErrors", jsonValidationLoggerHelper.toJsonObject((JsonSchemaValidationException) exception.getCause()));
        }

        return status(BAD_REQUEST)
                .entity(builder.build().toString())
                .build();
    }

}
