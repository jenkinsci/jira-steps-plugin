package org.thoughtslive.jenkins.plugins.jira.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.Site.LoginType;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.net.URL;

public class JiraServiceTest {

  @Rule
  public JenkinsRule j = new JenkinsRule();
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8888));

  private JiraService jiraService;

  @Before
  public void setup() throws IOException {
    UsernamePasswordCredentialsImpl c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "test1", null, "user1", "mypassword");
    SystemCredentialsProvider.getInstance().getCredentials().add(c);

    Site site = new Site("TEST", new URL(wireMockRule.baseUrl()), LoginType.CREDENTIAL.name(), 10000);
    site.setCredentialsId(c.getId());

    jiraService = new JiraService(site);
  }

  @Test
  public void test_userSearch() throws Exception {
    String url = "/rest/api/2/user/search?query=aQuery&startAt=0&maxResults=100";
    wireMockRule.stubFor(get(url).willReturn(aResponse().withBody("{\"result\":\"some\"}")));

    assertThat(jiraService.userSearch("aQuery", 0, 100).getData()).isEqualTo(ImmutableMap.of("result","some"));
  }
}
