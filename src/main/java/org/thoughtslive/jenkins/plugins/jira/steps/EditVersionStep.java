package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Version;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to update given JIRA verion.
 * 
 * @author Naresh Rayapati
 *
 */
public class EditVersionStep extends BasicJiraStep {

  private static final long serialVersionUID = -2029161404995143511L;

  @Getter
  private final Version version;

  @DataBoundConstructor
  public EditVersionStep(final Version version) {
    this.version = version;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "jiraEditVersion";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Edit Version";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    private static final long serialVersionUID = -5317591315201131186L;

    @StepContextParameter
    transient Run<?, ?> run;

    @StepContextParameter
    transient TaskListener listener;

    @StepContextParameter
    transient EnvVars envVars;

    @Inject
    transient EditVersionStep step;

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Updating version: " + step.getVersion());
        final String description = addMeta(step.getVersion().getDescription());
        step.getVersion().setDescription(description);
        response = jiraService.updateVersion(step.getVersion());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      // TODO Add validation - Or change the input type here ?
      return verifyCommon(step, listener, envVars, run);
    }
  }
}
