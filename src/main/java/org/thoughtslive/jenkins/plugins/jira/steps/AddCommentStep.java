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
 * Step to create a new JIRA comment.
 *
 * @author Naresh Rayapati
 */
public class AddCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = 8523118063993121080L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String comment;

  @Getter
  private final String roleVisibility;
  
  @DataBoundConstructor
  public AddCommentStep(final String idOrKey, final String comment, final String roleVisibility) {
    this.idOrKey = idOrKey;
    this.comment = comment;
    this.roleVisibility = roleVisibility;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraAddComment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Add Comment";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 462945562138805176L;

    private final AddCommentStep step;

    protected Execution(final AddCommentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Add new comment: " + step.getComment()
            + " on issue: " + step.getIdOrKey());
        final String comment =
            step.isAuditLog() ? addPanelMeta(step.getComment()) : step.getComment();
        response = jiraService.addComment(step.getIdOrKey(), comment, step.getRoleVisibility());
      }
      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {

      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        final String comment = Util.fixEmpty(step.getComment());

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (comment == null) {
          errorMessage = "comment is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
