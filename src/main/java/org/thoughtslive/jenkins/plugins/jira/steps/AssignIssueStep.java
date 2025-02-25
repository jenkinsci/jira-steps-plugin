package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import java.io.Serial;

import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to assign a JIRA Issue to given user.
 *
 * @author Naresh Rayapati
 */
@Getter
public class AssignIssueStep extends BasicJiraStep {

  @Serial
  private static final long serialVersionUID = -7552691123209663987L;

  private final String idOrKey;

  private final String userName;

  private final String accountId;

  @DataBoundConstructor
  public AssignIssueStep(final String idOrKey, final String userName, final String accountId) {
    this.idOrKey = idOrKey;
    this.userName = userName;
    this.accountId = accountId;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraAssignIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Assign Issue";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    @Serial
    private static final long serialVersionUID = -7608114889563811741L;

    private final AssignIssueStep step;

    protected Execution(final AssignIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        final String userName =
            Util.fixEmpty(step.getUserName()) == null ? Util.fixEmpty(step.getAccountId())
                : Util.fixEmpty(step.getUserName());
        logger.println("JIRA: Site - " + siteName + " - Assigning issue: " + step.getIdOrKey()
            + " to: " + userName);
        response = jiraService.assignIssue(step.getIdOrKey(), userName);
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
