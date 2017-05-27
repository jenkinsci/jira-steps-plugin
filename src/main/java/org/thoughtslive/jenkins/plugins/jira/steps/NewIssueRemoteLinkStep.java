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
 * Step to create a JIRA remote Issue Link..
 *
 * @author Naresh Rayapati
 */
public class NewIssueRemoteLinkStep extends BasicJiraStep {

  private static final long serialVersionUID = 3529709240318435576L;

  @Getter
  private final String idOrKey;

  @Getter
  private final Object remoteLink;

  @DataBoundConstructor
  public NewIssueRemoteLinkStep(final String idOrKey, final Object remoteLink) {
    this.idOrKey = idOrKey;
    this.remoteLink = remoteLink;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNewIssueRemoteLink";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create new remote link for given issue.";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 325576266548671174L;

    private final NewIssueRemoteLinkStep step;

    protected Execution(final NewIssueRemoteLinkStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Creating new remote link for " + step.getIdOrKey());
        response = jiraService.createIssueRemoteLink(step.getIdOrKey(), step.getRemoteLink());
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
