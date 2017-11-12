package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.InputBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to create a new JIRA Version.
 *
 * @author Naresh Rayapati
 */
public class NewVersionStep extends BasicJiraStep {

  private static final long serialVersionUID = -528328534268615694L;

  @Getter
  private final Object version;

  @DataBoundConstructor
  public NewVersionStep(final Object version) {
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
      return "jiraNewVersion";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create New Version";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 7109283776054289821L;

    private final NewVersionStep step;

    protected Execution(final NewVersionStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger
            .println("JIRA: Site - " + siteName + " - Creating new version: " + step.getVersion());

        if (step.isAuditLog()) {
          final String description = addMeta("");
          InputBuilder.appendDescription(step.getVersion(), description);
        }

        response = jiraService.createVersion(step.getVersion());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      return response;
    }
  }
}
