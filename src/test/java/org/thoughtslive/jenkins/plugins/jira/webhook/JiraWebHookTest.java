package org.thoughtslive.jenkins.plugins.jira.webhook;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class JiraWebHookTest {

  private JiraWebHook webhook;
  private StaplerRequest req;
  private StaplerResponse resp;

  @Before
  public void setup() {
    webhook = new JiraWebHook();
    req = mock(StaplerRequest.class);
    resp = mock(StaplerResponse.class);
  }

  @Test
  public void noWebHookEvent() throws Exception {

    StringReader sr = new StringReader("{}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_BAD_REQUEST);
  }

  @Test
  public void notImplementedEvent() throws Exception {

    StringReader sr = new StringReader("{ \"webhookEvent\": \"jira:issue_created\"}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_NOT_IMPLEMENTED);
  }

  @Test
  public void noIssue() throws Exception {

    StringReader sr = new StringReader("{ \"webhookEvent\": \"jira:issue_updated\"}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_BAD_REQUEST);
  }

  @Test
  public void noIssueKey() throws Exception {

    StringReader sr = new StringReader("{ \"webhookEvent\": \"jira:issue_updated\", \"issue\": {}}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_BAD_REQUEST);
  }

  @Test
  public void noChangeLog() throws Exception {

    StringReader sr = new StringReader("{ \"webhookEvent\": \"jira:issue_updated\", \"issue\": {\"key\":\"TST-1\"}}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_BAD_REQUEST);
  }

  @Test
  public void noChangeLogItems() throws Exception {

    StringReader sr = new StringReader(
        "{ \"webhookEvent\": \"jira:issue_updated\", \"issue\": {\"key\":\"TST-1\"}, \"changelog\":{}}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_BAD_REQUEST);
  }

  @Test
  public void changeLogItems() throws Exception {

    StringReader sr = new StringReader(
        "{ \"webhookEvent\": \"jira:issue_updated\", \"issue\": {\"key\":\"TST-1\"}, \"changelog\":{\"items\":{}}}");
    BufferedReader br = new BufferedReader(sr);

    when(req.getReader()).thenReturn(br);
    webhook.doNotify(req, resp);
    verify(resp, times(1)).setStatus(StaplerResponse.SC_NO_CONTENT);
  }

}
