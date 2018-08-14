package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import java.io.IOException;
import java.util.Map;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to retrieve JIRA ServerInfo.
 *
 * @author Stuart Rowe
 */
public class GetServerInfoStep extends BasicJiraStep {

  private static final long serialVersionUID = -439860819128513604L;

  @DataBoundConstructor
  public GetServerInfoStep() {
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraGetServerInfo";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Get Server Info";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Map<String, Object>>> {

    private static final long serialVersionUID = -3058199854899051131L;

    private final GetServerInfoStep step;

    protected Execution(final GetServerInfoStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Map<String, Object>> run() throws Exception {

      ResponseData<Map<String, Object>> response =  verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Get Server Info");
        response = jiraService.getServerInfo();
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      return verifyCommon(step);
    }
  }
}
