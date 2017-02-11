package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comments;
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
 * Step to query a JIRA Issue comments.
 *
 * @author Naresh Rayapati
 */
public class GetCommentsStep extends BasicJiraStep {

  private static final long serialVersionUID = 3545679919575498803L;

  @Getter
  private final String idOrKey;

  @DataBoundConstructor
  public GetCommentsStep(final String idOrKey) {
    this.idOrKey = idOrKey;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "jiraGetComments";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Issue Comments";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Comments>> {

    private static final long serialVersionUID = -9014641944773640463L;

    @StepContextParameter
    transient Run<?, ?> run;

    @StepContextParameter
    transient TaskListener listener;

    @StepContextParameter
    transient EnvVars envVars;

    @Inject
    transient GetCommentsStep step;

    @Override
    protected ResponseData<Comments> run() throws Exception {

      ResponseData<Comments> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Querying issue: " + step.getIdOrKey() + " comments");
        response = jiraService.getComments(step.getIdOrKey());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step, listener, envVars, run);

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
