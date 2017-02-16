package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.SearchResult;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;

/**
 * Step to search JIRAs by JQL.
 * 
 * @author Naresh Rayapati
 *
 */
public class JqlSearchStep extends BasicJiraStep {

  private static final long serialVersionUID = -7754102811625753132L;

  @Getter
  private final String jql;

  @DataBoundConstructor
  public JqlSearchStep(final String jql) {
    this.jql = jql;
  }

  // startAt is optional and defaults to 0.
  @Getter
  @DataBoundSetter
  private int startAt = 0;

  // maxResults is optional and defaults to 1000.
  // TODO this can't be more than 2000 as JIRA may shutdown with out of memory issues.
  @Getter
  @DataBoundSetter
  private int maxResults = 1000;

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

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<SearchResult>> {

    private static final long serialVersionUID = 3640953129479843111L;

    private final JqlSearchStep step;

    protected Execution(final JqlSearchStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<SearchResult> run() throws Exception {

      ResponseData<SearchResult> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Search JQL: " + step.getJql() + " startAt: "
            + step.getStartAt() + " maxResults: " + step.getMaxResults());
        response = jiraService.searchIssues(step.getJql(), step.getStartAt(), step.getMaxResults());
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
