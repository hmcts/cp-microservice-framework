package uk.gov.justice.services.jmx.api.mbean;

import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;

import java.util.List;
import java.util.UUID;

import javax.management.MXBean;

@MXBean
public interface SystemCommanderMBean {

    UUID call(final String systemCommandName);
    UUID callWithRuntimeId(final String systemCommandName, final UUID commandRuntimeId);
    List<SystemCommandDetails> listCommands();
    SystemCommandStatus getCommandStatus(UUID commandId);
}
