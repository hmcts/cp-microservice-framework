package uk.gov.justice.services.jmx.runner;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.framework.utilities.exceptions.StackTraceProvider;
import uk.gov.justice.services.jmx.api.SystemCommandException;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.command.SystemCommandHandlerProxy;
import uk.gov.justice.services.jmx.command.SystemCommandStore;
import uk.gov.justice.services.jmx.command.TestCommand;
import uk.gov.justice.services.jmx.command.TestCommandWithRuntimeId;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandRunnerTest {

    @Mock
    private SystemCommandStore systemCommandStore;

    @Mock
    private StackTraceProvider stackTraceProvider;

    @Mock
    private Logger logger;

    @InjectMocks
    private SystemCommandRunner systemCommandRunner;

    @Test
    public void shouldFindTheCorrectProxyForTheCommandAndInvoke() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = of(randomUUID());

        final SystemCommandHandlerProxy systemCommandHandlerProxy = mock(SystemCommandHandlerProxy.class);

        when(systemCommandStore.findCommandProxy(testCommand)).thenReturn(systemCommandHandlerProxy);

        systemCommandRunner.run(testCommand, commandId, commandRuntimeId);

        verify(systemCommandHandlerProxy).invokeCommand(testCommand, commandId, commandRuntimeId);
    }

    @Test
    public void shouldThrowSystemCommandFailedExceptionIfCommandFails() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = empty();
        final SystemCommandException systemCommandException = new SystemCommandException("Ooops");
        final String stackTrace = "stack trace";

        final SystemCommandHandlerProxy systemCommandHandlerProxy = mock(SystemCommandHandlerProxy.class);

        when(systemCommandStore.findCommandProxy(testCommand)).thenReturn(systemCommandHandlerProxy);
        doThrow(systemCommandException).when(systemCommandHandlerProxy).invokeCommand(testCommand, commandId, commandRuntimeId);
        when(stackTraceProvider.getStackTrace(systemCommandException)).thenReturn(stackTrace);

        try {
            systemCommandRunner.run(testCommand, commandId, commandRuntimeId);
            fail();
        } catch (final SystemCommandInvocationFailedException expected) {
            assertThat(expected.getMessage(), is("Failed to run System Command 'TEST_COMMAND'. Caused by uk.gov.justice.services.jmx.api.SystemCommandException: Ooops"));
            assertThat(expected.getServerStackTrace(), is(stackTrace));
            assertThat(expected.getCause(), is(nullValue()));
        }
    }

    @Test
    public void shouldThrowSystemCommandFailedExceptionWIthRuntimeIdIfCommandFails() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommandWithRuntimeId testCommand = new TestCommandWithRuntimeId();
        final Optional<UUID> commandRuntimeId = of(fromString("f060aaa2-c1bc-4fe8-8992-f67e76400945"));
        final SystemCommandException systemCommandException = new SystemCommandException("Ooops");
        final String stackTrace = "stack trace";

        final SystemCommandHandlerProxy systemCommandHandlerProxy = mock(SystemCommandHandlerProxy.class);

        when(systemCommandStore.findCommandProxy(testCommand)).thenReturn(systemCommandHandlerProxy);
        doThrow(systemCommandException).when(systemCommandHandlerProxy).invokeCommand(testCommand, commandId, commandRuntimeId);
        when(stackTraceProvider.getStackTrace(systemCommandException)).thenReturn(stackTrace);

        try {
            systemCommandRunner.run(testCommand, commandId, commandRuntimeId);
            fail();
        } catch (final SystemCommandInvocationFailedException expected) {
            assertThat(expected.getMessage(), is("Failed to run System Command 'TEST_COMMAND' for eventId 'f060aaa2-c1bc-4fe8-8992-f67e76400945'. Caused by uk.gov.justice.services.jmx.api.SystemCommandException: Ooops"));
            assertThat(expected.getServerStackTrace(), is(stackTrace));
            assertThat(expected.getCause(), is(nullValue()));
        }
    }
}
