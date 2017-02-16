package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;

/**
 * Step to query a JIRA Projects.
 *
 * @author Naresh Rayapati
 */
public class GetProjectsStep extends BasicJiraStep {

  private static final long serialVersionUID = 2689031885988669114L;

  @DataBoundConstructor
  public GetProjectsStep() {}

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetProjects";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Projects";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Project[]>> {

    private static final long serialVersionUID = -5702548715847670073L;

    private final GetProjectsStep step;

    protected Execution(final GetProjectsStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Project[]> run() throws Exception {

      ResponseData<Project[]> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Querying All Projects");
        response = jiraService.getProjects();
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      return verifyCommon(step);
    }
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }
}
