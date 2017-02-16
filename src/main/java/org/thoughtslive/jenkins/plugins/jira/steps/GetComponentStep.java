package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import lombok.Getter;

/**
 * Step to query a JIRA Component.
 *
 * @author Naresh Rayapati
 */
public class GetComponentStep extends BasicJiraStep {

  private static final long serialVersionUID = 387862257528432812L;

  @Getter
  private final int id;

  @DataBoundConstructor
  public GetComponentStep(final int id) {
    this.id = id;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetComponent";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Component";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Component>> {

    private static final long serialVersionUID = 211769231724671924L;

    private final GetComponentStep step;

    protected Execution(final GetComponentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Component> run() throws Exception {

      ResponseData<Component> response = verifyInput();

      if (response == null) {
        logger
            .println("JIRA: Site - " + siteName + " - Querying component with id: " + step.getId());
        response = jiraService.getComponent(step.getId());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        if (step.getId() <= 0) {
          errorMessage = "id less than or equals to zero.";
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
