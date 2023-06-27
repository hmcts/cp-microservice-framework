package uk.gov.justice.services.adapter.rest.interceptor;

import static jakarta.json.Json.createObjectBuilder;

import uk.gov.justice.services.adapter.rest.multipart.FileInputDetails;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@ApplicationScoped
public class FileInputDetailsHandler {

    @Inject
    SingleFileInputDetailsService singleFileInputDetailsService;

    public UUID store(final FileInputDetails fileInputDetails) {

        final String fileName = fileInputDetails.getFileName();

        final JsonObject metadata = createObjectBuilder()
                .add("fileName", fileName)
                .build();

        return singleFileInputDetailsService.store(fileInputDetails, metadata);
    }
}
