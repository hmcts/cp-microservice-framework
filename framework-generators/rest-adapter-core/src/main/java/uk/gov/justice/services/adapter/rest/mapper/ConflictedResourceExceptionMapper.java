package uk.gov.justice.services.adapter.rest.mapper;

import static jakarta.json.Json.createObjectBuilder;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.status;

import uk.gov.justice.services.adapter.rest.exception.ConflictedResourceException;

import jakarta.inject.Inject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;

@Provider
public class ConflictedResourceExceptionMapper implements ExceptionMapper<ConflictedResourceException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(final ConflictedResourceException exception) {
        logger.debug("Conflict", exception);

        final JsonObjectBuilder builder = createObjectBuilder()
                .add("error", exception.getMessage())
                .add("id", exception.getConflictingId().toString());

        return status(CONFLICT)
                .entity(builder.build().toString())
                .build();
    }

}
