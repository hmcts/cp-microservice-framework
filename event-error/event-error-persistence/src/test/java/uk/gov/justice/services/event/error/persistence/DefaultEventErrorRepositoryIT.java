package uk.gov.justice.services.event.error.persistence;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.core.error.PersistableEventError;
import uk.gov.justice.services.jdbc.persistence.ViewStoreJdbcDataSourceProvider;
import uk.gov.justice.services.test.utils.persistence.TestJdbcDataSourceProvider;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultEventErrorRepositoryIT {

    private final TestJdbcDataSourceProvider testJdbcDataSourceProvider = new TestJdbcDataSourceProvider();

    @Mock
    private ViewStoreJdbcDataSourceProvider viewStoreJdbcDataSourceProvider;

    @InjectMocks
    private DefaultEventErrorRepository defaultEventErrorRepository;

    @Test
    public void shouldSaveAndFind() throws Exception {

        final DataSource viewStoreDataSource = testJdbcDataSourceProvider.getViewStoreDataSource("framework");
        when(viewStoreJdbcDataSourceProvider.getDataSource()).thenReturn(viewStoreDataSource);

        final UUID id = randomUUID();
        final String hash = "576b975aff05b7f2b4a1f7b26eb47aa5";
        final String exceptionClassName = "uk.gov.justice.SomeException";
        final String exceptionMessage = "We're all going to die";
        final String causeClassName = "uk.gov.justice.servcies.SomeCauseException";
        final String causeMessage = "Oh no!";
        final String javaClassname = "uk.gov.justice.SomeJavaClass";
        final String javaMethod = "someMethod";
        final int javaLineNumber = 23;
        final String eventName = "some-context.events.something-happened";
        final UUID eventId = randomUUID();
        final UUID streamId = randomUUID();
        final ZonedDateTime dateCreated = new UtcClock().now();
        final String fullStackTrace = "the full stack trace";

        defaultEventErrorRepository.save(new PersistableEventError(
                id,
                hash,
                exceptionClassName,
                exceptionMessage,
                causeClassName,
                causeMessage,
                javaClassname,
                javaMethod,
                javaLineNumber,
                eventName,
                eventId,
                streamId,
                dateCreated,
                fullStackTrace));

        final Optional<PersistableEventError> persistableEventErrorOptional = defaultEventErrorRepository.findBy(id);
        
        assertThat(persistableEventErrorOptional.isPresent(), is(true));

        final PersistableEventError persistableEventError = persistableEventErrorOptional.get();

        assertThat(persistableEventError.getHash(), is(hash));
        assertThat(persistableEventError.getExceptionClassName(), is(exceptionClassName));
        assertThat(persistableEventError.getExceptionMessage(), is(exceptionMessage));
        assertThat(persistableEventError.getCauseClassName(), is(causeClassName));
        assertThat(persistableEventError.getCauseMessage(), is(causeMessage));
        assertThat(persistableEventError.getJavaClassname(), is(javaClassname));
        assertThat(persistableEventError.getJavaMethod(), is(javaMethod));
        assertThat(persistableEventError.getJavaLineNumber(), is(javaLineNumber));
        assertThat(persistableEventError.getEventName(), is(eventName));
        assertThat(persistableEventError.getEventId(), is(eventId));
        assertThat(persistableEventError.getStreamId(), is(streamId));
        assertThat(persistableEventError.getDateCreated(), is(dateCreated));
        assertThat(persistableEventError.getFullStackTrace(), is(fullStackTrace));
    }
}