package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to add watcher to issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class AddWatcherStep extends BasicJiraStep {

  private static final long serialVersionUID = 6417829072320454268L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String userName;

  @DataBoundConstructor
  public AddWatcherStep(final String idOrKey, final String userName) {
    this.idOrKey = idOrKey;
    this.userName = userName;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "jiraAddWatcher";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Add Watcher";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    private static final long serialVersionUID = 937198146137084269L;

    @StepContextParameter
    transient Run<?, ?> run;

    @StepContextParameter
    transient TaskListener listener;

    @StepContextParameter
    transient EnvVars envVars;

    @Inject
    transient AddWatcherStep step;

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Adding " + step.getUserName()
            + " to issue: " + step.getIdOrKey() + " as a watcher.");
        response = jiraService.addIssueWatcher(step.getIdOrKey(), step.getUserName());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step, listener, envVars, run);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        final String userName = Util.fixEmpty(step.getUserName());

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (userName == null) {
          errorMessage = "userName is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
