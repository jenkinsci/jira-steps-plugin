package org.thoughtslive.jenkins.plugins.jira.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
public class JiraWebHookCrumbExclusionTest {

  private JiraWebHookCrumbExclusion exclusion;
  private HttpServletRequest req;
  private HttpServletResponse resp;
  private FilterChain chain;

  @Before
  public void before() {
    exclusion = new JiraWebHookCrumbExclusion();
    req = mock(HttpServletRequest.class);
    resp = mock(HttpServletResponse.class);
    chain = mock(FilterChain.class);
  }

  @Test
  public void testFullPath() throws Exception {
    when(req.getPathInfo()).thenReturn("/jira-steps-webhook/");
    assertThat(exclusion.process(req, resp, chain)).isTrue();
    verify(chain, times(1)).doFilter(req, resp);
  }

  @Test
  public void testFullPathWithoutSlash() throws Exception {
    when(req.getPathInfo()).thenReturn("/jira-steps-webhook");
    assertThat(exclusion.process(req, resp, chain)).isFalse();
    verify(chain, never()).doFilter(req, resp);
  }

  @Test
  public void testInvalidPath() throws Exception {
    when(req.getPathInfo()).thenReturn("/some-other-url/");
    assertThat(exclusion.process(req, resp, chain)).isFalse();
    verify(chain, never()).doFilter(req, resp);
  }

  @Test
  public void testNullPath() throws Exception {
    when(req.getPathInfo()).thenReturn(null);
    assertThat(exclusion.process(req, resp, chain)).isFalse();
    verify(chain, never()).doFilter(req, resp);
  }

  @Test
  public void testEmptyPath() throws Exception {
    when(req.getPathInfo()).thenReturn("");
    assertThat(exclusion.process(req, resp, chain)).isFalse();
    verify(chain, never()).doFilter(req, resp);
  }
}
