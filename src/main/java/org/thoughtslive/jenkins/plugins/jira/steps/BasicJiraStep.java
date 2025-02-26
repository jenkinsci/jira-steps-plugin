package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.ProxyWhitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.StaticWhitelist;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Base class for all JIRA steps
 *
 * @author Naresh Rayapati
 */
public abstract class BasicJiraStep extends Step implements Serializable {

  private static final long serialVersionUID = 7268920801605705697L;

  @Getter
  @DataBoundSetter
  public String site;

  @Getter
  @DataBoundSetter
  public boolean failOnError = true;

  @Getter
  @DataBoundSetter
  public boolean auditLog = true;

  @Getter
  @DataBoundSetter
  public Map<String, String> queryParams = new HashMap<>();

  @Extension
  public static class JiraWhitelist extends ProxyWhitelist {

    public JiraWhitelist() throws IOException {
      super(new StaticWhitelist(
          "method org.thoughtslive.jenkins.plugins.jira.api.ResponseData getData"
      ));
    }
  }
}
