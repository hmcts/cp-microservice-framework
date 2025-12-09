package uk.gov.justice.services.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.justice.services.common.exception.ForbiddenRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

@Provider
public class ForbiddenRequestExceptionMapper implements ExceptionMapper<ForbiddenRequestException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForbiddenRequestExceptionMapper.class);

    @Override
    public Response toResponse(final ForbiddenRequestException exception) {
        LOGGER.debug("Forbidden Request", exception);

        return Response.status(FORBIDDEN)
                .entity(getJsonBuilderFactory().createObjectBuilder().add("error", exception.getMessage()).build().toString())
                .build();
    }

}
