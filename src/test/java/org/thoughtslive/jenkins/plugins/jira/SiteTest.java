package org.thoughtslive.jenkins.plugins.jira;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.net.URL;
import java.util.Base64;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.thoughtslive.jenkins.plugins.jira.Site.LoginType;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class SiteTest {

  @Rule
  public JenkinsRule j = new JenkinsRule();
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8888));

  @Test
  public void test_credentials() throws Exception {
    String username = "user1";
    String password = "mypassword";
    String issueId = "ISSUE-1";
    String commentId = "1";

    UsernamePasswordCredentialsImpl c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "test1", null, username, password);
    SystemCredentialsProvider.getInstance().getCredentials().add(c);

    Site site = new Site("TEST", new URL(wireMockRule.baseUrl()), LoginType.CREDENTIAL.name(), 10000);
    site.setCredentialsId(c.getId());

    String url = "/rest/api/2/issue/" + issueId + "/comment/" + commentId;
    wireMockRule.stubFor(get(url).willReturn(aResponse().withBody("{}")));
    site.getService().getComment(issueId, commentId);

    String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    wireMockRule.verify(getRequestedFor(urlEqualTo(url)).withHeader("Authorization", equalTo("Basic " + token)));
  }
}
