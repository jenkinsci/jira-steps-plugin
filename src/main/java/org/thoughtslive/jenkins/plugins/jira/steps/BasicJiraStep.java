package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import java.io.IOException;
import java.io.Serializable;
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
  private String site;

  @Getter
  @DataBoundSetter
  private boolean failOnError = true;

  @Getter
  @DataBoundSetter
  private boolean auditLog = true;

  @Extension
  public static class JiraWhitelist extends ProxyWhitelist {

    public JiraWhitelist() throws IOException {
      super(new StaticWhitelist(
          "method org.thoughtslive.jenkins.plugins.jira.api.ResponseData getData"
      ));
    }
  }
}
