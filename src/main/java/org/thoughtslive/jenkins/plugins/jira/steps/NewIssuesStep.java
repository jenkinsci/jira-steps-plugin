package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.Issues;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;

/**
 * Step to create a new JIRA Issues.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewIssuesStep extends BasicJiraStep {

  private static final long serialVersionUID = -1390437007976428509L;

  @Getter
  private final Issues issues;

  @DataBoundConstructor
  public NewIssuesStep(final Issues issues) {
    this.issues = issues;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNewIssues";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create New Issues";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = -7395311395671768027L;

    private final NewIssuesStep step;

    protected Execution(final NewIssuesStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Creating new Issues: " + step.getIssues());
        for (Issue issue : step.getIssues().getIssueUpdates()) {
          final String description =
              step.isAuditLog() ? addPanelMeta(issue.getFields().getDescription())
                  : issue.getFields().getDescription();
          issue.getFields().setDescription(description);
        }
        response = jiraService.createIssues(step.getIssues());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final Issues issues = step.getIssues();

        for (Issue issue : issues.getIssueUpdates()) {
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

          if (issue.getFields().getIssuetype() == null) {
            errorMessage = "fields->issuetype is null.";
            return buildErrorResponse(new RuntimeException(errorMessage));
          }

          if (issue.getFields().getProject() == null) {
            errorMessage = "fields->project is null.";
            return buildErrorResponse(new RuntimeException(errorMessage));
          }

          if (errorMessage != null) {
            logger.println(
                "The error here can be of from any of the issues. Please check all the issues.");
            response = buildErrorResponse(new RuntimeException(errorMessage));
            break;
          }
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
