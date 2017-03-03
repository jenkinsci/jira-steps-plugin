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
 * Step to query a JIRA Issue comment.
 *
 * @author Naresh Rayapati
 */
public class GetCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = -3225315653270733874L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String commentId;

  @DataBoundConstructor
  public GetCommentStep(final String idOrKey, final String commentId) {
    this.commentId = commentId;
    this.idOrKey = idOrKey;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetComment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Issue Comment";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Comment>> {

    private static final long serialVersionUID = 6956525377031302225L;

    private final GetCommentStep step;

    protected Execution(final GetCommentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Comment> run() throws Exception {

      ResponseData<Comment> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Querying issue: " + step.getIdOrKey()
            + " comment with id: " + step.getCommentId());
        response = jiraService.getComment(step.getIdOrKey(), step.getCommentId());
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

        if (idOrKey == null) {
          errorMessage = "idOrKey is empty or null.";
        }

        if (commentId == null) {
          errorMessage = "commentId is empty or null.";
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
