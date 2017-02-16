package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssues;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssuesInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import lombok.Getter;

/**
 * Step to create a new JIRA Issues.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewIssuesStep extends BasicJiraStep {

  private static final long serialVersionUID = -1390437007976428509L;

  @Getter
  private final IssuesInput issues;

  @DataBoundConstructor
  public NewIssuesStep(final IssuesInput issues) {
    this.issues = issues;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraNewIssues";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Create New Issues";
    }

    @Override
    public boolean isMetaStep() {
      return true;
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<BasicIssues>> {

    private static final long serialVersionUID = -7395311395671768027L;

    private final NewIssuesStep step;

    protected Execution(final NewIssuesStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<BasicIssues> run() throws Exception {

      ResponseData<BasicIssues> response = verifyInput();

      if (response == null) {
        logger.println("JIRA: Site - " + siteName + " - Creating new Issues: " + step.getIssues());
        for (IssueInput issue : step.getIssues().getIssueUpdates()) {
          final String description = addPanelMeta(issue.getFields().getDescription());
          issue.getFields().setDescription(description);
        }
        response = jiraService.createIssues(step.getIssues());
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
