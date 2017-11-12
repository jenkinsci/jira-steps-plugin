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
 * Step to notify issue.
 *
 * @author Naresh Rayapati
 */
public class NotifyIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = -5286750553487650184L;

  @Getter
  private final String idOrKey;

  @Getter
  private final Object notify;

  @DataBoundConstructor
  public NotifyIssueStep(final String idOrKey, final Object notify) {
    this.idOrKey = idOrKey;
    this.notify = notify;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNotifyIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Notify Issue";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    private static final long serialVersionUID = 2997765348391402484L;

    private final NotifyIssueStep step;

    protected Execution(final NotifyIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Notifing Issue: " + step.getIdOrKey());
        response = jiraService.notifyIssue(step.getIdOrKey(), step.getNotify());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        final Object notify = step.getNotify();

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (notify == null) {
          errorMessage = "notify is null.";
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
