package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to update given JIRA verion.
 *
 * @author Naresh Rayapati
 */
public class EditVersionStep extends BasicJiraStep {

  private static final long serialVersionUID = -2029161404995143511L;

  @Getter
  private final String id;

  @Getter
  private final Object version;

  @DataBoundConstructor
  public EditVersionStep(final String id, final Object version) {
    this.id = id;
    this.version = version;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
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

        response = jiraService.updateVersion(step.getId(), step.getVersion());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {

        if (step.getId() == null) {
          errorMessage = "id is empty or null.";
        }

        if (step.getVersion() == null) {
          errorMessage = "version is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
