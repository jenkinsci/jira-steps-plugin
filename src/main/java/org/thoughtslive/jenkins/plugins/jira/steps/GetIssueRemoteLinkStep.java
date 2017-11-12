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
 * Step to query a JIRA remote Issue Link by linkId.
 *
 * @author Naresh Rayapati
 */
public class GetIssueRemoteLinkStep extends BasicJiraStep {

  private static final long serialVersionUID = 3529709240318435576L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String linkId;

  @DataBoundConstructor
  public GetIssueRemoteLinkStep(final String idOrKey, final String linkId) {
    this.idOrKey = idOrKey;
    this.linkId = linkId;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetIssueRemoteLink";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Issue's Remote Link by linkId.";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 325576266548671174L;

    private final GetIssueRemoteLinkStep step;

    protected Execution(final GetIssueRemoteLinkStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Querying Issue's " + step.getIdOrKey()
                + " remote link by linkId:" + step.getLinkId());
        response = jiraService.getIssueRemoteLink(step.getIdOrKey(), step.getLinkId());
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

        if (Util.fixEmpty(step.getLinkId()) == null) {
          errorMessage = "linkId is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
