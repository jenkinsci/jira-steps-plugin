package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.Serializable;
import lombok.Getter;
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
}
