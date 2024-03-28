package uk.gov.justice.services.jmx.runner;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

public class RunSystemCommandTaskTest {

    @Test
    public void shouldRunTheSystemCommand() throws Exception {

        final UUID commandId = randomUUID();
        final SystemCommandRunner systemCommandRunner = mock(SystemCommandRunner.class);
        final SystemCommand systemCommand = mock(SystemCommand.class);
        final Optional<UUID> commandRuntimeId = empty();

        final RunSystemCommandTask runSystemCommandTask = new RunSystemCommandTask(
                systemCommandRunner,
                systemCommand,
                commandId,
                commandRuntimeId
        );

        runSystemCommandTask.call();

        verify(systemCommandRunner).run(systemCommand, commandId, commandRuntimeId);
    }
}
