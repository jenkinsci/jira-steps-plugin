package org.thoughtslive.jenkins.plugins.jira.steps;

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
 * Step to create a new JIRA component.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewComponentStep extends BasicJiraStep {

  private static final long serialVersionUID = 4939494003115851145L;

  @Getter
  private final Component component;

  @DataBoundConstructor
  public NewComponentStep(final Component component) {
    this.component = component;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNewComponent";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create New Component";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Component>> {

    private static final long serialVersionUID = -6324419009842564119L;

    private final NewComponentStep step;

    protected Execution(final NewComponentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Component> run() throws Exception {

      ResponseData<Component> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Creating new component: " + step.getComponent());
        final String description = addMeta(step.getComponent().getDescription());
        step.getComponent().setDescription(description);
        response = jiraService.createComponent(step.getComponent());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      // TODO Add validation - Or change the input type here ?
      return verifyCommon(step);
    }
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }
}
