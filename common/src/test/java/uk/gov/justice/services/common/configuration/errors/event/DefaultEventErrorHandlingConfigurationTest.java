package uk.gov.justice.services.common.configuration.errors.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultEventErrorHandlingConfigurationTest {

    @InjectMocks
    private DefaultEventErrorHandlingConfiguration eventErrorHandlingConfiguration;

    @Test
    public void shouldGetEventStreamSelfHealingEnabledBooleanFromJndi() throws Exception {

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "true");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "false");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));
    }

    @Test
    public void shouldHandleNullsDifferentCaseEtc() throws Exception {

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "TRUE");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "true");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "tRUe");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(true));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "false");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "FALSE");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "False");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", "faLSe");
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));

        setField(eventErrorHandlingConfiguration, "eventStreamSelfHealingEnabled", null);
        assertThat(eventErrorHandlingConfiguration.isEventStreamSelfHealingEnabled(), is(false));
    }
}