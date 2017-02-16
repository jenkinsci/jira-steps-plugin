package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.TransitionInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.Extension;
import lombok.Getter;

/**
 * Step to transition JIRA issue.
 *
 * @author Naresh Rayapati
 */
public class TransitionIssueStep extends BasicJiraStep {

  private static final long serialVersionUID = 5648167982018270684L;

  @Getter
  private final String idOrKey;

  @Getter
  private final TransitionInput input;

  @DataBoundConstructor
  public TransitionIssueStep(final String idOrKey, final TransitionInput input) {
    this.idOrKey = idOrKey;
    this.input = input;
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraTransitionIssue";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Transition Issue";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    private static final long serialVersionUID = 6038231959460139190L;

    private final TransitionIssueStep step;

    protected Execution(final TransitionIssueStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Transition issue with idOrKey: " + step.getIdOrKey());
        response = jiraService.transitionIssue(step.getIdOrKey(), step.getInput());
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
