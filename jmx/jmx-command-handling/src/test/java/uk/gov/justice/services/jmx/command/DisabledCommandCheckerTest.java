package uk.gov.justice.services.jmx.command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.subscription.pull.EventPullConfiguration;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DisabledCommandCheckerTest {

    @Mock
    private EventPullConfiguration eventPullConfiguration;
    
    @InjectMocks
    private DisabledCommandChecker disabledCommandChecker;

    @Test
    public void shouldReturnTrueIfCommandIsMarkedAsDisabledAndPullMechanismIsEnable() throws Exception {

        final SystemCommand disabledSystemCommand = new DisabledSystemCommand();
        final SystemCommand validSystemCommand = new ValidSystemCommand();

        when(eventPullConfiguration.shouldProcessEventsByPullMechanism()).thenReturn(true);

        assertThat(disabledCommandChecker.isDisabledByPullMechanism(disabledSystemCommand), is(true));
        assertThat(disabledCommandChecker.isDisabledByPullMechanism(validSystemCommand), is(false));

    }

    @Test
    public void shouldReturnFalseIfPullMechanismIsNotEnabled() throws Exception {

        final SystemCommand disabledSystemCommand = new DisabledSystemCommand();
        final SystemCommand validSystemCommand = new ValidSystemCommand();

        when(eventPullConfiguration.shouldProcessEventsByPullMechanism()).thenReturn(false);

        assertThat(disabledCommandChecker.isDisabledByPullMechanism(disabledSystemCommand), is(false));
        assertThat(disabledCommandChecker.isDisabledByPullMechanism(validSystemCommand), is(false));
    }

    private static class DisabledSystemCommand implements SystemCommand {

        @Override
        public String getName() {
            return "DISABLED_SYSTEM_COMMAND";
        }

        @Override
        public String getDescription() {
            return "This command is disabled by pull mechanism";
        }

        @Override
        public boolean isDisabledByPullMechanism() {
            return true;
        }
    }

    private static class ValidSystemCommand implements SystemCommand {

        @Override
        public String getName() {
            return "VALID_SYSTEM_COMMAND";
        }

        @Override
        public String getDescription() {
            return "This command is NOT disabled by pull mechanism";
        }

        @Override
        public boolean isDisabledByPullMechanism() {
            return false;
        }
    }
}