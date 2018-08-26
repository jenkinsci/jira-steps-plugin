package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.support.actions.PauseAction;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.webhook.JiraIssueChangeListener;
import org.thoughtslive.jenkins.plugins.jira.webhook.JiraWebHook;

import com.google.common.collect.ImmutableSet;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Step to wait for an issue field to be updated.
 *
 * @author Ludovic Cintrat
 */
public class WaitForIssueUpdateStep extends Step implements Serializable {

  private static final long serialVersionUID = -6061636623882913257L;

  private final String idOrKey;

  private final String field;

  private final String[] fieldValues;

  @DataBoundConstructor
  public WaitForIssueUpdateStep(final String idOrKey, final String field, final String[] fieldValues) {

    this.idOrKey = idOrKey;
    this.field = field;
    this.fieldValues = Arrays.copyOf(fieldValues, fieldValues.length);
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  static class Execution extends StepExecution implements JiraIssueChangeListener {

    private static final long serialVersionUID = -4298923001851046514L;

    private static final Logger LOG = Logger.getLogger(Execution.class.getName());

    private final WaitForIssueUpdateStep step;

    public Execution(WaitForIssueUpdateStep step, StepContext context) {
      super(context);
      this.step = step;
    }

    @Override
    public boolean start() throws Exception {
      log(
          String.format(
              "Waiting for issue %s [%s in (%s)]",
              step.idOrKey,
              step.field,
              String.join(", ", step.fieldValues)));

      FlowNode node = getContext().get(FlowNode.class);
      node.addAction(new PauseAction("Jira WebHook"));

      JiraWebHook.get().registerWebhook(this);

      return false;
    }

    @Override
    public void onResume() {
      super.onResume();
      JiraWebHook.get().registerWebhook(this);
    }

    @Override
    public void stop(Throwable cause) throws Exception {
      PauseAction.endCurrentPause(getContext().get(FlowNode.class));
      JiraWebHook.get().unregisterWebhook(this);
      getContext().onFailure(cause);
    }

    private boolean match(String field, String fieldValue) {
      return step.field.equals(field) && ArrayUtils.contains(step.fieldValues, fieldValue);
    }

    @Override
    public boolean onIssueUpdated(String field, String fieldValue) {
      try {
        if (match(field, fieldValue)) {
          PauseAction.endCurrentPause(getContext().get(FlowNode.class));
          getContext().onSuccess(fieldValue);

          log(String.format("Jira issue %s updated [%s = %s]", step.idOrKey, field, fieldValue));
          return true;
        } else {
          return false;
        }
      } catch (IOException | InterruptedException e) {
        LOG.warning("onIssueUpdated: " + e);
        return true;
      }
    }

    @Override
    public String getIssueIdOrKey() {
      return step.idOrKey;
    }

    private void log(String msg, Object... args) throws IOException, InterruptedException {
      getContext().get(TaskListener.class).getLogger().printf(msg, args);
      getContext().get(TaskListener.class).getLogger().println();
    }
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraWaitForIssueUpdate";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Wait for an issue update";
    }

    @Override
    public Set<Class<?>> getRequiredContext() {
      return ImmutableSet.of(FlowNode.class, Run.class, TaskListener.class);
    }
  }
}
