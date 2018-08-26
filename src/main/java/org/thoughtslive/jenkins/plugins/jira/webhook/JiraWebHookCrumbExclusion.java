package org.thoughtslive.jenkins.plugins.jira.webhook;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.common.annotations.VisibleForTesting;

import hudson.Extension;
import hudson.security.csrf.CrumbExclusion;

@Extension
public class JiraWebHookCrumbExclusion extends CrumbExclusion {

  @VisibleForTesting
  public final Map<String, List<JiraIssueChangeListener>> listeners = new HashMap<>();

  @Override
  public boolean process(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    String pathInfo = req.getPathInfo();

    if (StringUtils.isEmpty(pathInfo)) {
      return false;
    }

    if (!pathInfo.startsWith(getExclusionPath())) {
      return false;
    }
    chain.doFilter(req, resp);
    return true;
  }

  public String getExclusionPath() {
    return "/" + JiraWebHook.URL_PATH + "/";
  }
}
