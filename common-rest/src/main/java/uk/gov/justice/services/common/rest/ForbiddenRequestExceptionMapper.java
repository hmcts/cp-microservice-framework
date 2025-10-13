package uk.gov.justice.services.common.rest;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import uk.gov.justice.services.common.exception.ForbiddenRequestException;

import static uk.gov.justice.services.messaging.JsonObjects.jsonBuilderFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ForbiddenRequestExceptionMapper implements ExceptionMapper<ForbiddenRequestException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForbiddenRequestExceptionMapper.class);

    @Override
    public Response toResponse(final ForbiddenRequestException exception) {
        LOGGER.debug("Forbidden Request", exception);

        return Response.status(FORBIDDEN)
                .entity(jsonBuilderFactory.createObjectBuilder().add("error", exception.getMessage()).build().toString())
                .build();
    }

}
