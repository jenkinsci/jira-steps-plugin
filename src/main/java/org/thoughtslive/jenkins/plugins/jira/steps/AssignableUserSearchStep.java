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
 * Step to search JIRA Users those can be assigned  for particular project, issue.
 *
 * @author Naresh Rayapati
 */
public class AssignableUserSearchStep extends BasicJiraStep {

  private static final long serialVersionUID = -7754102811625753132L;

  @Getter
  private String project;

  @Getter
  private String issueKey;
  @Getter
  @DataBoundSetter
  private String queryStr;
  @Getter
  @DataBoundSetter
  private int startAt = 0;
  @Getter
  @DataBoundSetter
  private int maxResults = 1000;

  @DataBoundConstructor
  public AssignableUserSearchStep(final String project, final String issueKey) {
    this.project = project;
    this.issueKey = issueKey;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraAssignableUserSearch";
    }

    @Override
    public String getDisplayName() {
      return getPrefix()
          + "Searches assignable JIRA Users by username, name or email address for the given project/issueKey";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 3640953129479843111L;

    private final AssignableUserSearchStep step;

    protected Execution(final AssignableUserSearchStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Searching assignable JIRA Users: " + step.getQueryStr()
                + " by project/issueKey: " + step.getProject() + " / " + step.getIssueKey()
                + " startAt: "
                + step.getStartAt() + " maxResults: " + step.getMaxResults());
        response = jiraService
            .assignableUserSearch(step.getQueryStr(), step.getProject(), step.getIssueKey(),
                step.getStartAt(), step.getMaxResults());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String project = Util.fixEmpty(step.getProject());
        final String issueKey = Util.fixEmpty(step.getIssueKey());

        if (project == null && issueKey == null) {
          errorMessage = "either project or issueKey is required.";
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
