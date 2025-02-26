package org.thoughtslive.jenkins.plugins.jira.service;

import static java.net.http.HttpResponse.BodyHandlers.ofByteArray;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hudson.ProxyConfiguration;
import junit.framework.TestCase;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class JiraServiceTest {
    public static MockWebServer jira;
    public static MockWebServer proxy;
    @ClassRule
    public static JenkinsRule jenkins = new JenkinsRule();

    @BeforeClass
    public static void setUpJiraMock() throws IOException {
        jira = new MockWebServer();
        jira.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getMethod().equals("GET")) {
                    switch (recordedRequest.getPath()) {
                    case "/rest/api/2/serverInfo": {
                        try {
                            return new MockResponse().setResponseCode(200)
                                .setBody(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                                    Map.of(
                                        "baseUrl", "https://jira",
                                        "version", "9.12.7",
                                        "versionNumbers", List.of(9, 12, 7),
                                        "deploymentType", "Server",
                                        "buildNumber", 9120007,
                                        "buildDate", "2024-04-11T00:00:00.000+0200",
                                        "databaseBuildNumber", 9120007,
                                        "scmInfo", "77878f28c5a5673469d81b7d9f14744ca4b2ad1e",
                                        "serverTitle", "Test JIRA"
                                    )
                                ));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    }
                }
                TestCase.fail("Unrecognized request: " + recordedRequest.getMethod() + " " + recordedRequest.getPath());
                return new MockResponse().setResponseCode(404);
            }
        });
        jira.start();
    }

    @AfterClass
    public static void stopJiraMock() throws IOException {
        jira.shutdown();
    }

    @BeforeClass
    public static void setUpProxy() throws IOException {
        proxy = new MockWebServer();
        proxy.setDispatcher(new Dispatcher() {
            final Pattern REQUEST_LINE = Pattern.compile("(?<method>[^ ]+) +(?<resource>[^ ]+) +(?<extra>.*)");
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getHeader("Proxy-Authorization") == null) {
                    return new MockResponse().setResponseCode(407).addHeader("Proxy-Authenticate", "Basic realm=test");
                }
                final Matcher matcher = REQUEST_LINE.matcher(recordedRequest.getRequestLine());
                if (!matcher.matches()) {
                    TestCase.fail("Invalid request: " + recordedRequest.getRequestLine());
                    return new MockResponse().setResponseCode(500);
                }
                String method = matcher.group("method");
                String resource = matcher.group("resource");
                if (!resource.startsWith(jira.url("/").toString())) {
                    TestCase.fail("Not a proxy request for the Jira mock: " + recordedRequest.getRequestLine());
                    return new MockResponse().setResponseCode(500);
                }
                URI uri = URI.create(resource);
                final HttpClient client = HttpClient.newBuilder().build();
                try {
                    final byte[] body = recordedRequest.getBody().readByteArray();
                    final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .method(method, HttpRequest.BodyPublishers.ofByteArray(body));
                    recordedRequest.getHeaders()
                        .toMultimap()
                        .entrySet()
                        .stream()
                        .filter(e -> !(List.of("connection", "proxy-authorization", "host").contains(e.getKey().toLowerCase())))
                        .forEach(e -> e.getValue().forEach(value -> requestBuilder.setHeader(e.getKey(), value)));
                    requestBuilder.uri(uri);
                    final HttpResponse<byte[]> response = client.send(requestBuilder.build(), ofByteArray());
                    try (final Buffer output = new Buffer()) {
                        output.write(response.body());
                        return new MockResponse().setResponseCode(response.statusCode()).setBody(output);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        proxy.start();
    }

    @AfterClass
    public static void stopProxy() throws IOException {
        proxy.shutdown();
    }

    @After
    public void clearProxyConfig() {
        jenkins.getInstance().setProxy(null);
    }

    @Test
    public void shouldCheckInfo() {
        Site site = new Site(
            "test", jira.url("/").url(), Site.LoginType.BASIC.name(), (int) Duration.ofMinutes(5).toMillis()
        );
        site.setUserName("scott");
        site.setPassword("tiger");
        JiraService service = new JiraService(site);
        final ResponseData<Map<String, Object>> serverInfo = service.getServerInfo();
        assertThat(serverInfo.isSuccessful()).isTrue();
        assertThat(serverInfo.getMessage()).isNotNull();
    }

    @Test
    public void shouldUseProxyAuthentication() throws InterruptedException {
        Site site = new Site(
            "test", jira.url("/").url(), Site.LoginType.BASIC.name(), (int) Duration.ofMinutes(5).toMillis()
        );
        site.setUserName("scott");
        site.setPassword("tiger");
        jenkins.getInstance().setProxy(new ProxyConfiguration("localhost", proxy.getPort(), "proxyuser", null));

        JiraService service = new JiraService(site);
        final ResponseData<Map<String, Object>> serverInfo = service.getServerInfo();
        assertThat(serverInfo.getCode()).isNotEqualTo(-1);
        assertThat(serverInfo.isSuccessful()).isTrue();
        RecordedRequest request = jira.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/rest/api/2/serverInfo");
    }

    @Test
    public void shouldTolerateUseProxyAuthenticationHavingNullUsername() {
        Site site = new Site(
            "test", jira.url("/").url(), Site.LoginType.BASIC.name(), (int) Duration.ofMinutes(5).toMillis()
        );
        site.setUserName("scott");
        site.setPassword("tiger");
        jenkins.getInstance().setProxy(new ProxyConfiguration("localhost", proxy.getPort(), null, null));

        JiraService service = new JiraService(site);
        final ResponseData<Map<String, Object>> serverInfo = service.getServerInfo();
        assertThat(serverInfo.getCode()).isNotEqualTo(-1);
        assertThat(serverInfo.isSuccessful()).isTrue();
    }
}
