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
import hudson.Util;
import lombok.Getter;

/**
 * Step to update the given JIRA component.
 * 
 * @author Naresh Rayapati
 *
 */
public class EditComponentStep extends BasicJiraStep {

  private static final long serialVersionUID = 6528605492208170984L;

  @Getter
  private final Component component;

  @DataBoundConstructor
  public EditComponentStep(final Component component) {
    this.component = component;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraEditComponent";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Edit Component";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    private static final long serialVersionUID = 6229925264184593843L;

    private final EditComponentStep step;

    protected Execution(final EditComponentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger
            .println("JIRA: Site - " + siteName + " - Updating component: " + step.getComponent());
        final String description = addMeta(step.getComponent().getDescription());
        step.getComponent().setDescription(description);
        response = jiraService.updateComponent(step.getComponent());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final int id = step.getComponent().getId();
        final String name = Util.fixEmpty(step.getComponent().getName());
        final String project = Util.fixEmpty(step.getComponent().getProject());

        if (id == 0) {
          errorMessage = "id required.";
        }

        if (name == null) {
          errorMessage = "name is empty or null.";
        }

        if (project == null) {
          errorMessage = "project is empty or null.";
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
