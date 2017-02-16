package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Version;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
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

    private final EditVersionStep step;

    protected Execution(final EditVersionStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

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
      return verifyCommon(step);
    }
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }
}
