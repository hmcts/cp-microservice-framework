package uk.gov.justice.services.clients.core.httpclient;

import uk.gov.justice.services.clients.core.HttpCaller;
import uk.gov.justice.services.clients.core.HttpCallerResponse;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Map;

/**
 * Default implementation of {@link HttpCaller} backed by the Java native {@link HttpClient}.
 * The underlying {@link HttpClient} instance is created once and cached for the lifetime of
 * this object. It is thread-safe and designed to be shared across concurrent requests,
 * maintaining its own internal connection pool
 */
public class DefaultHttpCaller implements HttpCaller {

    private HttpClient httpClient;

    public DefaultHttpCaller() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public HttpCallerResponse get(final String url, final Map<String, Object> headers) {
        final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        if (headers != null) {
            headers.forEach((name, value) -> requestBuilder.header(name, String.valueOf(value)));
        }

        try {
            final HttpResponse<String> response = httpClient.send(
                    requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            return new HttpCallerResponse(response.statusCode(), response.body());
        } catch (final HttpTimeoutException e) {
            throw new HttpCallerException("GET request timed out for URL: " + url, e);
        } catch (final ConnectException e) {
            throw new HttpCallerException("Could not connect to URL: " + url, e);
        } catch (final IOException e) {
            throw new HttpCallerException("GET request failed for URL: " + url, e);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpCallerException("GET request was interrupted for URL: " + url, e);
        }
    }
}
