package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import com.google.common.collect.ImmutableMap;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;
import org.thoughtslive.jenkins.plugins.jira.api.InputBuilder;

/**
 * Step to create a new JIRA comment.
 *
 * @author Naresh Rayapati
 */
public class AddCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = 8523118063993121080L;

  @Getter
  private final String idOrKey;

  @Deprecated
  @Getter
  private final String comment;

  @Getter
  @DataBoundSetter
  private Object input;
  
  @Deprecated
  @DataBoundConstructor
  public AddCommentStep(final String idOrKey, final String comment) {
    this.idOrKey = idOrKey;
    this.comment = comment;
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
        if (step.getComment() != null) {
          logger.println("JIRA: Site - " + siteName + " - Add new comment (deprecated): " + step.getComment()
              + " on issue: " + step.getIdOrKey());
          final String comment =
              step.isAuditLog() ? addPanelMeta(step.getComment()) : step.getComment();
          response = jiraService.addComment(step.getIdOrKey(), ImmutableMap.builder().put("body", comment).build());
        } else {
          logger.println("JIRA: Site - " + siteName + " - Add new comment: " + step.getInput()
              + " on issue: " + step.getIdOrKey());
          final String message = InputBuilder.getField(step.getInput(), "body") != null 
              ? InputBuilder.getField(step.getInput(), "body").toString()
              : "";
          final String comment =
              step.isAuditLog() ? addPanelMeta(message) : message;
          InputBuilder.setField(step.getInput(), "body", comment);
          response = jiraService.addComment(step.getIdOrKey(), step.getInput());
        }
      }
      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {

      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String idOrKey = Util.fixEmpty(step.getIdOrKey());

        if (step.getComment() != null && step.getInput() != null) {
          errorMessage = "Use either comment or input.";
        }

        if (step.getComment() == null && step.getInput() == null) {
          errorMessage = "You need to set at least comment or input.";
        }

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (step.getComment() != null && step.getComment().isEmpty()) {
          errorMessage = "comment is empty.";
        }

        if (step.getInput() != null 
          && (InputBuilder.getField(step.getInput(), "body") == null 
            || InputBuilder.getField(step.getInput(), "body").toString().isEmpty())) {
          errorMessage = "input body is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
