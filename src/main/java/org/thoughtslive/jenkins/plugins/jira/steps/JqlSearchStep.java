package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to search JIRAs by JQL.
 *
 * @author Naresh Rayapati
 */
public class JqlSearchStep extends BasicJiraStep {

  private static final long serialVersionUID = -7754102811625753132L;

  @Getter
  public final String jql;

  @Getter
  @DataBoundSetter
  public int startAt = 0;

  @Getter
  @DataBoundSetter
  public int maxResults = 1000;

  @Getter
  @DataBoundSetter
  public Object fields;

  @DataBoundConstructor
  public JqlSearchStep(final String jql) {
    this.jql = jql;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraJqlSearch";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "JQL Search";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 3640953129479843111L;

    private final JqlSearchStep step;

    protected Execution(final JqlSearchStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Search JQL: " + step.getJql() + " startAt: "
            + step.getStartAt() + " maxResults: " + step.getMaxResults());
        response = jiraService
            .searchIssues(step.getJql(), step.getStartAt(), step.getMaxResults(), step.getFields());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String jql = Util.fixEmpty(step.getJql());

        if (jql == null) {
          errorMessage = "jql is empty or null.";
        }

        if (step.getMaxResults() > 2000) {
          errorMessage = "maxResults can't be more than 2000 to avoid memory issues.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
