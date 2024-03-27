package uk.gov.justice.services.jmx.runner;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RunSystemCommandTask implements Callable<Boolean> {

    private final SystemCommandRunner systemCommandRunner;
    private final SystemCommand systemCommand;
    private final UUID commandId;
    private final Optional<UUID> commandRuntimeId;

    public RunSystemCommandTask(
            final SystemCommandRunner systemCommandRunner,
            final SystemCommand systemCommand,
            final UUID commandId,
            final Optional<UUID> commandRuntimeId) {
        this.systemCommandRunner = systemCommandRunner;
        this.systemCommand = systemCommand;
        this.commandId = commandId;
        this.commandRuntimeId = commandRuntimeId;
    }

    @Override
    public Boolean call() throws Exception {

        systemCommandRunner.run(systemCommand, commandId, commandRuntimeId);

        return true;
    }
}
