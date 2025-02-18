package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import java.io.Serial;

import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to search JIRA Users.
 *
 * @author Naresh Rayapati
 */
@Getter
public class UserSearchStep extends BasicJiraStep {

  @Serial
  private static final long serialVersionUID = -7754102811625753132L;

  private final String queryStr;
  @DataBoundSetter
  private int startAt = 0;
  @DataBoundSetter
  private int maxResults = 1000;

  @DataBoundConstructor
  public UserSearchStep(final String queryStr) {
    this.queryStr = queryStr;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraUserSearch";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Search Active JIRA Users by username, name or email address.";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    @Serial
    private static final long serialVersionUID = 3640953129479843111L;

    private final UserSearchStep step;

    protected Execution(final UserSearchStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Searching JIRA Active Users: " + step.getQueryStr()
                + " startAt: "
                + step.getStartAt() + " maxResults: " + step.getMaxResults());
        response = jiraService
            .userSearch(step.getQueryStr(), step.getStartAt(), step.getMaxResults());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String queryStr = Util.fixEmpty(step.getQueryStr());

        if (queryStr == null) {
          errorMessage = "queryStr is empty or null.";
        }

        if (step.getMaxResults() > 1000) {
          errorMessage = "maxResults: The maximum allowed value is 1000.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
