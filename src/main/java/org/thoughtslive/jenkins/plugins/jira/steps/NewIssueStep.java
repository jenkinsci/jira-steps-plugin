package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to create a new JIRA Issue.
 *
 * @author Naresh Rayapati
 */
public class NewIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = -3952881085849787165L;

  @Getter
  private final IssueInput issue;

  @DataBoundConstructor
  public NewIssueStep(final IssueInput issue) {
    this.issue = issue;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNewIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create New Issue";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 2782781910330634547L;

    private final NewIssueStep step;

    protected Execution(final NewIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Creating new issue: " + step.getIssue());
        if (step.getIssue().getFields().get("description") != null) {
          String description = step.getIssue().getFields().get("description").toString();
          description = step.isAuditLog() ? addPanelMeta(description) : description;
          step.getIssue().getFields().put("description", description);
        } else {
          if (step.isAuditLog()) {
            step.getIssue().getFields().put("description", addPanelMeta(""));
          }
        }
        response = jiraService.createIssue(step.getIssue());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final IssueInput issue = step.getIssue();

        if (issue == null) {
          errorMessage = "issue is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (issue.getFields() == null) {
          errorMessage = "fields is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (issue.getFields().get("summary") == null
            || Util.fixEmpty(issue.getFields().get("summary").toString()) == null) {
          errorMessage = "fields->summary is empty or null.";
        }

        if (issue.getFields().get("issuetype") == null) {
          errorMessage = "fields->issuetype is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));

        }

        if (issue.getFields().get("project") == null) {
          errorMessage = "fields->project is null.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
