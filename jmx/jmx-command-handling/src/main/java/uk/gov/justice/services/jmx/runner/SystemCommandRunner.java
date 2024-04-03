package uk.gov.justice.services.jmx.runner;

import static java.lang.String.format;
import static javax.transaction.Transactional.TxType.NEVER;

import uk.gov.justice.services.framework.utilities.exceptions.StackTraceProvider;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.command.SystemCommandStore;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

public class SystemCommandRunner {

    @Inject
    private SystemCommandStore systemCommandStore;

    @Inject
    private StackTraceProvider stackTraceProvider;

    @Inject
    private Logger logger;

    @Transactional(NEVER)
    public void run(final SystemCommand systemCommand, final UUID commandId, final Optional<UUID> commandRuntimeId) {

        try {

            if(commandRuntimeId.isPresent())  {
                logger.info(format("Running system command '%s' with %s '%s'", systemCommand.getName(), systemCommand.commandRuntimeIdType(), commandRuntimeId.get()));
            } else {
                logger.info("Running system command '%s'");
            }

            systemCommandStore.findCommandProxy(systemCommand).invokeCommand(systemCommand, commandId, commandRuntimeId);
        } catch (final Throwable e) {
            final StringBuilder message = new StringBuilder(format("Failed to run System Command '%s'", systemCommand.getName()));

            commandRuntimeId.ifPresent(commandRuntimeUuid -> message.append(format(" for %s '%s'", systemCommand.commandRuntimeIdType(), commandRuntimeUuid)));

            logger.error(message.toString(), e);

            throw new SystemCommandInvocationFailedException(
                    message.append(". Caused by ")
                            .append(e.getClass().getName())
                            .append(": ")
                            .append(e.getMessage())
                            .toString(),
                    stackTraceProvider.getStackTrace(e));
        }
    }
}
