package uk.gov.justice.services.adapter.rest.interceptor;

import uk.gov.justice.services.adapter.rest.multipart.FileInputDetails;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.UUID;

import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

@ApplicationScoped
public class FileInputDetailsHandler {

    @Inject
    SingleFileInputDetailsService singleFileInputDetailsService;

    public UUID store(final FileInputDetails fileInputDetails) {

        final String fileName = fileInputDetails.getFileName();

        final JsonObject metadata = getJsonBuilderFactory().createObjectBuilder()
                .add("fileName", fileName)
                .build();

        return singleFileInputDetailsService.store(fileInputDetails, metadata);
    }
}
