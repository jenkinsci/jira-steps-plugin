package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;

/**
 * Step to delete JIRA remote Issue Links by globalId.
 *
 * @author Naresh Rayapati
 */
public class DeleteIssueRemoteLinksStep extends BasicJiraStep {

  private static final long serialVersionUID = 3529709240318435576L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String globalId;

  @DataBoundConstructor
  public DeleteIssueRemoteLinksStep(final String idOrKey, final String globalId) {
    this.idOrKey = idOrKey;
    this.globalId = globalId;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraDeleteIssueRemoteLinks";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Delete Issue's Remote Links by globalId.";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 325576266548671174L;

    private final DeleteIssueRemoteLinksStep step;

    protected Execution(final DeleteIssueRemoteLinksStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Deleting Issue's " + step.getIdOrKey() + " Remote links Link by globalId:" + step.getGlobalId());
        response = jiraService.deleteIssueRemoteLinks(step.getIdOrKey(), step.getGlobalId());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        if (Util.fixEmpty(step.getIdOrKey()) == null) {
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
