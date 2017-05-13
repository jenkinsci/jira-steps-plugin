package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;

/**
 * Step to update JIRA issue comment.
 * 
 * @author Naresh Rayapati
 *
 */
public class EditCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = -6330276534463853856L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String commentId;

  @Getter
  private final String comment;

  @DataBoundConstructor
  public EditCommentStep(final String idOrKey, final String commentId, final String comment) {
    this.idOrKey = idOrKey;
    this.commentId = commentId;
    this.comment = comment;
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

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Comment>> {

    private static final long serialVersionUID = -7000442485946132663L;

    private final EditCommentStep step;

    protected Execution(final EditCommentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Comment> run() throws Exception {

      ResponseData<Comment> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Updating comment: " + step.getComment()
            + " on issue: " + step.getIdOrKey());
        response =
            jiraService.updateComment(step.getIdOrKey(), step.getCommentId(), step.getComment());
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
        final String commentId = Util.fixEmpty(step.getCommentId());

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (commentId == null) {
          errorMessage = "commentId is empty or null.";
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

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }
}
