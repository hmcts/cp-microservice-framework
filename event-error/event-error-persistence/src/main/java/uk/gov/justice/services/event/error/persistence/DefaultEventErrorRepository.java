package uk.gov.justice.services.event.error.persistence;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.core.error.EventErrorRepository;
import uk.gov.justice.services.core.error.PersistableEventError;
import uk.gov.justice.services.jdbc.persistence.ViewStoreJdbcDataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class DefaultEventErrorRepository implements EventErrorRepository {

    private static final String INSERT_EXCEPTION_SQL = """
            INSERT INTO event_error (
                id,
                hash,         
                exception_classname,
                exception_message,
                cause_classname,
                cause_message,
                java_classname,
                java_method,
                java_line_number,
                event_name,
                event_id,
                stream_id,
                date_created,
                full_stack_trace
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT
                hash,
                exception_classname,
                exception_message,
                cause_classname,
                cause_message,
                java_classname,
                java_method,
                java_line_number,
                event_name,
                event_id,
                stream_id,
                date_created,
                full_stack_trace
            FROM event_error
            WHERE id = ?
            """;

    @Inject
    private ViewStoreJdbcDataSourceProvider viewStoreDataSourceProvider;

    @Transactional(REQUIRES_NEW)
    @Override
    public void save(final PersistableEventError persistableEventError) {

        try (final Connection connection = viewStoreDataSourceProvider.getDataSource().getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EXCEPTION_SQL)) {

            final UUID id = persistableEventError.getId();
            final String hash = persistableEventError.getHash();
            final String exceptionClassName = persistableEventError.getExceptionClassName();
            final String exceptionMessage = persistableEventError.getExceptionMessage();
            final String causeClassName = persistableEventError.getCauseClassName();
            final String causeMessage = persistableEventError.getCauseMessage();
            final String javaClassname = persistableEventError.getJavaClassname();
            final String javaMethod = persistableEventError.getJavaMethod();
            final int javaLineNumber = persistableEventError.getJavaLineNumber();
            final String eventName = persistableEventError.getEventName();
            final UUID eventId = persistableEventError.getEventId();
            final UUID streamId = persistableEventError.getStreamId();
            final ZonedDateTime dateCreated = persistableEventError.getDateCreated();
            final String fullStackTrace = persistableEventError.getFullStackTrace();

            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, exceptionClassName);
            preparedStatement.setString(4, exceptionMessage);
            preparedStatement.setString(5, causeClassName);
            preparedStatement.setString(6, causeMessage);
            preparedStatement.setString(7, javaClassname);
            preparedStatement.setString(8, javaMethod);
            preparedStatement.setInt(9, javaLineNumber);
            preparedStatement.setString(10, eventName);
            preparedStatement.setObject(11, eventId);
            preparedStatement.setObject(12, streamId);
            preparedStatement.setTimestamp(13, toSqlTimestamp(dateCreated));
            preparedStatement.setString(14, fullStackTrace);

            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException("Ooops", e);
        }
    }

    public Optional<PersistableEventError> findBy(final UUID id) {

        try (final Connection connection = viewStoreDataSourceProvider.getDataSource().getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setObject(1, id);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final String hash = resultSet.getString("hash");
                    final String exceptionClassName = resultSet.getString("exception_classname");
                    final String exceptionMessage = resultSet.getString("exception_message");
                    final String causeClassName = resultSet.getString("cause_classname");
                    final String causeMessage = resultSet.getString("cause_message");
                    final String javaClassname = resultSet.getString("java_classname");
                    final String javaMethod = resultSet.getString("java_method");
                    final int javaLineNumber = resultSet.getInt("java_line_number");
                    final String eventName = resultSet.getString("event_name");
                    final UUID eventId = (UUID) resultSet.getObject("event_id");
                    final UUID streamId = (UUID) resultSet.getObject("stream_id");
                    final ZonedDateTime dateCreated = ZonedDateTimes.fromSqlTimestamp(resultSet.getTimestamp("date_created"));
                    final String fullStackTrace = resultSet.getString("full_stack_trace");

                    final PersistableEventError persistableEventError = new PersistableEventError(
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
                            fullStackTrace
                    );

                    return of(persistableEventError);
                }

                return empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ooops", e);
        }
    }
}
