package uk.gov.justice.services.jmx.runner;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

@RunWith(MockitoJUnitRunner.class)
public class RunSystemCommandTaskTest {

    private static final UUID commandId = randomUUID();
    private static final Optional<UUID> commandRuntimeId = empty();

    @Mock
    private SystemCommandRunner systemCommandRunner;

    @Mock
    private SystemCommand systemCommand;

    @Mock
    private SystemCommandInvocationFailureHandler systemCommandInvocationFailureHandler;

    private RunSystemCommandTask runSystemCommandTask;

    @Before
    public void setUp() {
        runSystemCommandTask = new RunSystemCommandTask(
                systemCommandRunner,
                systemCommand,
                commandId,
                commandRuntimeId,
                systemCommandInvocationFailureHandler
        );
    }

    @Test
    public void shouldRunTheSystemCommand() throws Exception {
        runSystemCommandTask.call();

        verify(systemCommandRunner).run(systemCommand, commandId, commandRuntimeId);
    }

    @Test
    public void onFailureShouldInvokeExceptionHandler() throws Exception {
        final RuntimeException exception = new RuntimeException();
        doThrow(exception).when(systemCommandRunner).run(systemCommand, commandId, commandRuntimeId);

        runSystemCommandTask.call();

        verify(systemCommandInvocationFailureHandler).handle(exception, systemCommand, commandId, commandRuntimeId);
    }
}
