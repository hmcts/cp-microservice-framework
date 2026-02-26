package uk.gov.justice.services.clients.core.httpclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.services.clients.core.HttpCallerResponse;

import java.util.HashMap;
import java.util.Map;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
public class DefaultHttpCallerTest {

    private static final String RESOURCE_PATH = "/api/resource";

    private DefaultHttpCaller httpCaller;
    private String url;

    @BeforeEach
    public void setUp(final WireMockRuntimeInfo wireMockRuntimeInfo) {
        httpCaller = new DefaultHttpCaller();
        url = wireMockRuntimeInfo.getHttpBaseUrl() + RESOURCE_PATH;
    }

    @Test
    public void shouldReturnResponseBodyAndStatusCodeOnSuccessfulGet() {
        final String responseBody = "{\"id\":\"abc-123\"}";

        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(responseBody)));

        final HttpCallerResponse response = httpCaller.get(url, Map.of());

        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getBody(), is(responseBody));
    }

    @Test
    public void shouldSendRequestHeadersWithGet() {
        final String correlationId = "trace-xyz-789";
        final String authToken = "Bearer some-token";

        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("X-Correlation-Id", equalTo(correlationId))
                .withHeader("Authorization", equalTo(authToken))
                .willReturn(aResponse().withStatus(200).withBody("")));

        final Map<String, Object> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("X-Correlation-Id", correlationId);
        headers.put("Authorization", authToken);

        final HttpCallerResponse response = httpCaller.get(url, headers);

        assertThat(response.getStatusCode(), is(200));
        verify(getRequestedFor(urlEqualTo(RESOURCE_PATH))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("X-Correlation-Id", equalTo(correlationId))
                .withHeader("Authorization", equalTo(authToken)));
    }

    @Test
    public void shouldHandleNullHeadersMapWithoutError() {
        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .willReturn(aResponse().withStatus(200).withBody("ok")));

        final HttpCallerResponse response = httpCaller.get(url, null);

        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getBody(), is("ok"));
    }

    @Test
    public void shouldReturnNotFoundStatus() {
        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .willReturn(aResponse().withStatus(404).withBody("Not Found")));

        final HttpCallerResponse response = httpCaller.get(url, Map.of());

        assertThat(response.getStatusCode(), is(404));
        assertThat(response.getBody(), is("Not Found"));
    }

    @Test
    public void shouldReturnEmptyBodyOnNoContentResponse() {
        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .willReturn(aResponse().withStatus(204)));

        final HttpCallerResponse response = httpCaller.get(url, Map.of());

        assertThat(response.getStatusCode(), is(204));
        assertThat(response.getBody(), is(""));
    }

    @Test
    public void shouldThrowHttpCallerExceptionOnConnectionFailure() {
        assertThrows(HttpCallerException.class, () -> httpCaller.get("http://localhost:1" + RESOURCE_PATH, Map.of()));
    }
}
