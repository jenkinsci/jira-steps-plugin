package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to update given JIRA issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class EditIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = -4542562652787306504L;

  @Getter
  private final String idOrKey;

  @Getter
  private final IssueInput issue;

  @DataBoundConstructor
  public EditIssueStep(final String idOrKey, final IssueInput issue) {
    this.idOrKey = idOrKey;
    this.issue = issue;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "jiraEditIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Edit Issue";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<BasicIssue>> {

    private static final long serialVersionUID = -4127725325057889625L;

    @StepContextParameter
    transient Run<?, ?> run;

    @StepContextParameter
    transient TaskListener listener;

    @StepContextParameter
    transient EnvVars envVars;

    @Inject
    transient EditIssueStep step;

    @Override
    protected ResponseData<BasicIssue> run() throws Exception {

      ResponseData<BasicIssue> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Updating issue: " + step.getIdOrKey());
        final String description = addPanelMeta(step.getIssue().getFields().getDescription());
        step.getIssue().getFields().setDescription(description);
        response = jiraService.updateIssue(step.getIdOrKey(), step.getIssue());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step, listener, envVars, run);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        // TODO Add validation - Or change the input type here ?
        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
