package uk.gov.justice.services.common.configuration.subscription.pull;

import static java.lang.Boolean.parseBoolean;

import uk.gov.justice.services.common.configuration.Value;
import uk.gov.justice.services.common.util.LazyValue;

import javax.inject.Inject;

public class EventPullConfiguration {

    private final LazyValue lazyValue = new LazyValue();

    @Inject
    @Value(key = "events.publishing.process.events.from.event.topic", defaultValue = "true")
    private String shouldProcessEventsFromEventTopic;


    public boolean shouldEventsFromEventTopic() {
        return  lazyValue.createIfAbsent(() -> parseBoolean(shouldProcessEventsFromEventTopic));
    }
}
