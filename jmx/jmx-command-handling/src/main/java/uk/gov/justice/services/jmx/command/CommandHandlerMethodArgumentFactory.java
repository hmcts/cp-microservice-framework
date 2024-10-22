package uk.gov.justice.services.jmx.command;

import static java.util.Optional.ofNullable;

import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommandHandlerMethodArgumentFactory {

    public Object[] createMethodArguments(
            final SystemCommand systemCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

        final Optional<UUID> commandRuntimeId = ofNullable(jmxCommandRuntimeParameters.getCommandRuntimeId());
        final List<Object> methodArguments = new ArrayList<>();
        methodArguments.add(systemCommand);
        methodArguments.add(commandId);

        commandRuntimeId.ifPresent(methodArguments::add);

        return methodArguments.toArray();
    }
}
