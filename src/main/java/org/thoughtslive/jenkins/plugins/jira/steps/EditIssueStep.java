package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to update given JIRA issue.
 *
 * @author Naresh Rayapati
 */
public class EditIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = -4542562652787306504L;

  @Getter
  private final String idOrKey;

  @Getter
  private final Object issue;

  @DataBoundConstructor
  public EditIssueStep(final String idOrKey, final Object issue) {
    this.idOrKey = idOrKey;
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
      return "jiraEditIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Edit Issue";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = -4127725325057889625L;

    private final EditIssueStep step;

    protected Execution(final EditIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Updating issue: " + step.getIdOrKey());
        response = jiraService
            .updateIssue(step.getIdOrKey(), step.getIssue(), step.getQueryParams());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        final Object issue = step.getIssue();

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (issue == null) {
          errorMessage = "issue is null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
