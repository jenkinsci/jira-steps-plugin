package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import com.google.common.collect.ImmutableMap;
import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.InputBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to update JIRA issue comment.
 *
 * @author Naresh Rayapati
 */
public class EditCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = -6330276534463853856L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String commentId;

  @Deprecated
  @Getter
  private final String comment;

  @Getter
  @DataBoundSetter
  private Object input;

  @Deprecated
  @DataBoundConstructor
  public EditCommentStep(final String idOrKey, final String commentId, final String comment) {
    this.idOrKey = idOrKey;
    this.commentId = commentId;
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
      return "jiraEditComment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Edit Issue Comment";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = -7000442485946132663L;

    private final EditCommentStep step;

    protected Execution(final EditCommentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        if (step.getComment() != null) {
          logger.println(
              "JIRA: Site - " + siteName + " - Updating comment (deprecated): " + step.getComment()
                  + " on issue: " + step.getIdOrKey());
          response = jiraService.updateComment(step.getIdOrKey(), step.getCommentId(),
              ImmutableMap.builder().put("body", step.getComment()).build());
        } else {
          logger.println("JIRA: Site - " + siteName + " - Updating comment: " + step.getInput()
              + " on issue: " + step.getIdOrKey());
          response = jiraService
              .updateComment(step.getIdOrKey(), step.getCommentId(), step.getInput());
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
        final String commentId = Util.fixEmpty(step.getCommentId());

        if (step.getComment() != null && step.getInput() != null) {
          errorMessage = "Use either comment or input.";
        }

        if (step.getComment() == null && step.getInput() == null) {
          errorMessage = "You need to set at least comment or input.";
        }

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (commentId == null) {
          errorMessage = "commentId is empty or null.";
        }

        if (step.getComment() != null && step.getComment().isEmpty()) {
          errorMessage = "comment is empty.";
        }

        if (step.getInput() != null
            && (InputBuilder.getField(step.getInput(), "body") == null
            || InputBuilder.getField(step.getInput(), "body").isEmpty())) {
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
