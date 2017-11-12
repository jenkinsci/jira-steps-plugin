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
 * Step to query a JIRA Issue comments.
 *
 * @author Naresh Rayapati
 */
public class GetCommentsStep extends BasicJiraStep {

  private static final long serialVersionUID = 3545679919575498803L;

  @Getter
  private final String idOrKey;

  @DataBoundConstructor
  public GetCommentsStep(final String idOrKey) {
    this.idOrKey = idOrKey;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetComments";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Issue Comments";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = -9014641944773640463L;

    private final GetCommentsStep step;

    protected Execution(final GetCommentsStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Querying issue: " + step.getIdOrKey() + " comments");
        response = jiraService.getComments(step.getIdOrKey());
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
}
