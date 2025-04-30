package uk.gov.justice.services.common.configuration.errors.event;

import static java.lang.Boolean.parseBoolean;

import uk.gov.justice.services.common.configuration.Value;

import javax.inject.Inject;

public class EventErrorHandlingConfiguration {

    @Inject
    @Value(key = "event.stream.self.healing.enabled", defaultValue = "false")
    private String eventStreamSelfHealingEnabled;

    public boolean isEventStreamSelfHealingEnabled() {
        return parseBoolean(eventStreamSelfHealingEnabled);
    }
}
