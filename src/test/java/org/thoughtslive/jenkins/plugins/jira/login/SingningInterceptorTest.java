package org.thoughtslive.jenkins.plugins.jira.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.jvnet.hudson.test.JenkinsRule;
import org.thoughtslive.jenkins.plugins.jira.Site;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;

import hudson.util.Secret;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class SingningInterceptorTest {
    @ClassRule
    public static JenkinsRule jenkins = new JenkinsRule();
    @Rule
    public TestName testName = new TestName();

    @NotNull
    private static OkHttpClient buildClient(Site jiraSite) {
        return new OkHttpClient.Builder()
            .addInterceptor(new SigningInterceptor(jiraSite)).build();
    }

    private static void addCredentials(Credentials credentials) throws IOException {
        Iterable<CredentialsStore> credentialsStores = CredentialsProvider.lookupStores(jenkins.jenkins);
        assumeThat(credentialsStores).hasSize(1);
        CredentialsStore store = credentialsStores.iterator().next();
        List<Domain> domains = store.getDomains();
        assumeThat(domains).isNotEmpty();
        store.addCredentials(domains.get(0), credentials);
    }

    private static MockWebServer mockWebServer() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        return server;
    }

    @Test
    public void shouldUseBasicAuthWithUsernameAndPasswordDirectlyFromConfigWhenConfiguredTo()
        throws IOException, InterruptedException {
        Site jiraSite = mock(Site.class);
        when(jiraSite.getLoginType()).thenReturn(Site.LoginType.BASIC.name());
        when(jiraSite.getUserName()).thenReturn("scott");
        when(jiraSite.getPassword()).thenReturn(Secret.fromString("tiger"));

        OkHttpClient client = buildClient(jiraSite);

        final RecordedRequest request;
        try (MockWebServer server = mockWebServer()) {
            client.newCall(new Request.Builder().url(server.url("/")).build()).execute().close();
            request = server.takeRequest();
        }
        assertThat(request.getHeader("authorization"))
            .isEqualTo("Basic " + Base64.getEncoder().encodeToString("scott:tiger".getBytes()));
    }

    @Test
    public void shouldUseBasicAuthWithUsernameAndPasswordFromCredentialsWhenConfiguredTo()
        throws IOException, InterruptedException {
        String credentialsId = testName.getMethodName();
        Site jiraSite = mock(Site.class);
        when(jiraSite.getLoginType()).thenReturn(Site.LoginType.CREDENTIAL.name());
        when(jiraSite.getCredentialsId()).thenReturn(credentialsId);

        addCredentials(
            new UsernamePasswordCredentialsImpl(
                CredentialsScope.GLOBAL, credentialsId, "Test credentials", "scott", "tiger"
            )
        );

        OkHttpClient client = buildClient(jiraSite);

        final RecordedRequest request;
        try (MockWebServer server = mockWebServer()) {
            client.newCall(new Request.Builder().url(server.url("/")).build()).execute().close();
            request = server.takeRequest();
        }
        assertThat(request.getHeader("authorization"))
            .isEqualTo("Basic " + Base64.getEncoder().encodeToString("scott:tiger".getBytes()));
    }
}
