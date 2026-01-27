package uk.gov.justice.services.common.configuration.subscription.pull;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventPullConfigurationTest {

    @InjectMocks
    private EventPullConfiguration eventPullConfiguration;

    @Test
    public void shouldParseTheJndiValueAsBooleanTrueIfTheConfigStringIsTrue() throws Exception {

        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", "true");
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(true));
    }

    @Test
    public void shouldParseTheJndiValueAsBooleanFalseIfTheConfigStringIsNotTrue() throws Exception {

        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", "something-not-true");
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(false));
    }

    @Test
    public void shouldCacheTheParsedBooleanInMemoryOnceParsed() throws Exception {

        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", "true");
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(true));
        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", "false");
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(true));
        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", "something-silly");
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(true));
        setField(eventPullConfiguration, "shouldProcessEventsFromEventTopic", null);
        assertThat(eventPullConfiguration.shouldProcessEventsFromEventTopic(), is(true));
    }
}