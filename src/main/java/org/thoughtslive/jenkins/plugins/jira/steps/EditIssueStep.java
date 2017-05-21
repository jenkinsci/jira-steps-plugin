package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
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
  private final Issue issue;

  @DataBoundConstructor
  public EditIssueStep(final String idOrKey, final Issue issue) {
    this.idOrKey = idOrKey;
    this.issue = issue;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

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

    private final EditIssueStep step;

    protected Execution(final EditIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<BasicIssue> run() throws Exception {

      ResponseData<BasicIssue> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Updating issue: " + step.getIdOrKey());
        response = jiraService.updateIssue(step.getIdOrKey(), step.getIssue());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        final Issue issue = step.getIssue();

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (issue == null) {
          errorMessage = "issue is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (issue.getFields() == null) {
          errorMessage = "fields is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }
}
