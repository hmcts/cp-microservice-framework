package uk.gov.justice.services.common.configuration.errors.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventErrorHandlingConfigurationTest {

    @InjectMocks
    private EventErrorHandlingConfiguration eventErrorHandlingConfiguration;

    @Test
    public void shouldGetTheEventErrorHandlingEnabledBooleanFromJndi() throws Exception {

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "true");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "false");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));
    }

    @Test
    public void shouldHandleNullsDifferentCaseEtc() throws Exception {

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "TRUE");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "true");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "tRUe");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "false");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "FALSE");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "False");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", "faLSe");
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventErrorHandlingEnabled", null);
        assertThat(eventErrorHandlingConfiguration.isEventErrorHandlingEnabled(), is(false));
    }
}