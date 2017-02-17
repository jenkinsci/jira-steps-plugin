package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
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
  private final IssueInput issue;

  @DataBoundConstructor
  public EditIssueStep(final String idOrKey, final IssueInput issue) {
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
        final String description = addPanelMeta(step.getIssue().getFields().getDescription());
        step.getIssue().getFields().setDescription(description);
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
        final IssueInput issue = step.getIssue();

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

        if (Util.fixEmpty(issue.getFields().getSummary()) == null) {
          errorMessage = "fields->summary is empty or null.";
        }

        if (Util.fixEmpty(issue.getFields().getDescription()) == null) {
          errorMessage = "fields->description is empty or null.";
        }

        if (issue.getFields().getIssuetype() == null) {
          errorMessage = "fields->issuetype is null.";
        }

        if (issue.getFields().getIssuetype().getId() == 0) {
          errorMessage = "fields->issuetype->id is zero or missing";
        }

        if (issue.getFields().getProject() == null) {
          errorMessage = "fields->project is null.";
        }

        if (issue.getFields().getProject().getId() == 0) {
          errorMessage = "fields->project->id is zero or missing";
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
