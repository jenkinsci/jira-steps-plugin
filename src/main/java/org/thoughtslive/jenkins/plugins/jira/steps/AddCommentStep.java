package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to create a new JIRA comment.
 * 
 * @author Naresh Rayapati
 *
 */
public class AddCommentStep extends BasicJiraStep {

  private static final long serialVersionUID = 8523118063993121080L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String comment;

  @DataBoundConstructor
  public AddCommentStep(final String idOrKey, final String comment) {
    this.idOrKey = idOrKey;
    this.comment = comment;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "jiraAddComment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Add Comment";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Comment>> {

    private static final long serialVersionUID = 462945562138805176L;

    @StepContextParameter
    transient Run<?, ?> run;

    @StepContextParameter
    transient TaskListener listener;

    @StepContextParameter
    transient EnvVars envVars;

    @Inject
    transient AddCommentStep step;

    @Override
    protected ResponseData<Comment> run() throws Exception {

      ResponseData<Comment> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Add new comment: " + step.getComment()
            + " on issue: " + step.getIdOrKey());
        final String comment = addPanelMeta(step.getComment());
        response = jiraService.addComment(step.getIdOrKey(), comment);
      }
      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {

      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step, listener, envVars, run);

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
