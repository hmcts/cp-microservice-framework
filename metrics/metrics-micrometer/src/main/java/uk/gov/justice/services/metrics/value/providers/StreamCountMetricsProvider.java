package uk.gov.justice.services.metrics.value.providers;

import uk.gov.justice.services.jdbc.persistence.ViewStoreJdbcDataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

public class StreamCountMetricsProvider {

    private static final int ZERO_STREAMS = 0;
    private static final String COUNT_STREAMS_SQL = """
        SELECT COUNT(*) AS number_of_streams
        FROM (
            SELECT DISTINCT stream_id FROM stream_status
        ) AS temp;
    """;

    @Inject
    private ViewStoreJdbcDataSourceProvider viewStoreJdbcDataSourceProvider;

    public int countTotalNumberOfStreams() {

        try (final Connection connection = viewStoreJdbcDataSourceProvider.getDataSource().getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(COUNT_STREAMS_SQL);
             final ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return ZERO_STREAMS;

        } catch (final SQLException e) {
            throw new MetricsProviderException("Failed to count total number of streams in stream_status table", e);
        }
    }
}
