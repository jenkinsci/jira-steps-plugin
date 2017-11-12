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
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to query a JIRA Issue Link.
 *
 * @author Naresh Rayapati
 */
public class GetIssueLinkStep extends BasicJiraStep {

  private static final long serialVersionUID = -4252560961571411897L;
  @Getter
  private final String id;

  @DataBoundConstructor
  public GetIssueLinkStep(final String id) {
    this.id = id;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetIssueLink";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get IssueLink";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 325576266548671174L;

    private final GetIssueLinkStep step;

    protected Execution(final GetIssueLinkStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Querying IssueLink by id:" + step.getId());
        response = jiraService.getIssueLink(step.getId());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        if (Util.fixEmpty(step.getId()) == null) {
          errorMessage = "id is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
