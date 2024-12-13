package uk.gov.justice.services.core.error;

import java.time.ZonedDateTime;
import java.util.UUID;

public class PersistableEventError {

    private final UUID id;
    private final String hash;
    private final String exceptionClassName;
    private final String exceptionMessage;
    private final String causeClassName;
    private final String causeMessage;
    private final String javaClassname;
    private final String javaMethod;
    private final int javaLineNumber;
    private final String eventName;
    private final UUID eventId;
    private final UUID streamId;
    private final ZonedDateTime dateCreated;
    private final String fullStackTrace;

    public PersistableEventError(
            final UUID id,
            final String hash,
            final String exceptionClassName,
            final String exceptionMessage,
            final String causeClassName,
            final String causeMessage,
            final String javaClassname,
            final String javaMethod,
            final int javaLineNumber,
            final String eventName,
            final UUID eventId,
            final UUID streamId,
            final ZonedDateTime dateCreated,
            final String fullStackTrace) {
        this.id = id;
        this.hash = hash;
        this.exceptionClassName = exceptionClassName;
        this.exceptionMessage = exceptionMessage;
        this.causeClassName = causeClassName;
        this.causeMessage = causeMessage;
        this.javaClassname = javaClassname;
        this.javaMethod = javaMethod;
        this.javaLineNumber = javaLineNumber;
        this.eventName = eventName;
        this.eventId = eventId;
        this.streamId = streamId;
        this.dateCreated = dateCreated;
        this.fullStackTrace = fullStackTrace;
    }

    public UUID getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getCauseClassName() {
        return causeClassName;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public String getJavaClassname() {
        return javaClassname;
    }

    public String getJavaMethod() {
        return javaMethod;
    }

    public int getJavaLineNumber() {
        return javaLineNumber;
    }

    public String getEventName() {
        return eventName;
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getStreamId() {
        return streamId;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public String getFullStackTrace() {
        return fullStackTrace;
    }
}
