package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;

/**
 * Step to query a JIRA Issue.
 *
 * @author Naresh Rayapati
 */
public class GetIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = -8758698444697767020L;

  @Getter
  private final String idOrKey;

  @DataBoundConstructor
  public GetIssueStep(final String idOrKey) {
    this.idOrKey = idOrKey;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Issue";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Issue>> {

    private static final long serialVersionUID = 6898696015602575211L;

    private final GetIssueStep step;

    protected Execution(final GetIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Issue> run() throws Exception {

      ResponseData<Issue> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Querying issue with idOrKey: " + step.getIdOrKey());
        response = jiraService.getIssue(step.getIdOrKey());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
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
