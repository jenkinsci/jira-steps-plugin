package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.jvnet.hudson.test.BuildWatcher;
import org.jvnet.hudson.test.JenkinsRule.JSONWebResponse;
import org.jvnet.hudson.test.RestartableJenkinsRule;
import org.kohsuke.stapler.StaplerResponse;
import org.thoughtslive.jenkins.plugins.jira.webhook.JiraWebHook;
import org.xml.sax.SAXException;

import hudson.model.Result;
import hudson.model.queue.QueueTaskFuture;

public class WaitForIssueUpdateStepTest {

  private static final String JOB_NAME1 = "testJob1";
  private static final String JOB_NAME2 = "testJob2";

  WaitForIssueUpdateStep.Execution stepExecution;

  @Rule
  public RestartableJenkinsRule story = new RestartableJenkinsRule();
  @ClassRule
  public static BuildWatcher bw = new BuildWatcher();

  @Test
  public void waitForStatusOk() throws InterruptedException, IOException, SAXException {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress', 'Done'");
        WorkflowRun b = pipeline.waitForStart();

        waitForStepToWait(1, b);
        JSONWebResponse rsp = submitWebHook("TEST-1", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline);
        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
      }
    });
  }

  @Test
  public void waitForStatusOk2Issues() throws InterruptedException, IOException, SAXException {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress', 'Done'");
        pipeline.waitForStart();

        QueueTaskFuture<WorkflowRun> pipeline2 = submitPipeline(JOB_NAME2, "TEST-2", "status", "'In Progress', 'Done'");
        WorkflowRun b2 = pipeline2.waitForStart();
        waitForStepToWait(2, b2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).hasSize(1);
        assertThat(JiraWebHook.get().listeners.get("TEST-2")).hasSize(1);

        JSONWebResponse rsp = submitWebHook("TEST-1", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
        assertThat(JiraWebHook.get().listeners.get("TEST-2")).hasSize(1);

        rsp = submitWebHook("TEST-2", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
        assertThat(JiraWebHook.get().listeners.get("TEST-2")).isNull();
      }
    });
  }

  @Test
  public void waitForStatusOk2Fields() throws InterruptedException, IOException, SAXException {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress', 'Done'");
        pipeline.waitForStart();

        QueueTaskFuture<WorkflowRun> pipeline2 = submitPipeline(JOB_NAME2, "TEST-1", "severity", "'High'");
        WorkflowRun b2 = pipeline2.waitForStart();
        waitForStepToWait(1, b2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).hasSize(2);

        JSONWebResponse rsp = submitWebHook("TEST-1", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).hasSize(1);

        rsp = submitWebHook("TEST-1", "severity", "High");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
      }
    });
  }

  @Test
  public void waitForStatusOk2Values() throws InterruptedException, IOException, SAXException {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress'");
        pipeline.waitForStart();

        QueueTaskFuture<WorkflowRun> pipeline2 = submitPipeline(JOB_NAME2, "TEST-1", "status", "'Done'");
        WorkflowRun b2 = pipeline2.waitForStart();
        waitForStepToWait(1, b2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).hasSize(2);

        JSONWebResponse rsp = submitWebHook("TEST-1", "status", "In Progress");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).hasSize(1);

        rsp = submitWebHook("TEST-1", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(pipeline2);

        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
      }
    });
  }

  @Test
  public void waitForStatusRestart() throws InterruptedException, IOException, SAXException {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress', 'Done'");
        WorkflowRun b = pipeline.waitForStart();

        waitForStepToWait(1, b);
      }
    });

    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        WorkflowJob p = story.j.jenkins.getItemByFullName(JOB_NAME1, WorkflowJob.class);
        WorkflowRun b = p.getLastBuild();
        JSONWebResponse rsp = submitWebHook("TEST-1", "status", "Done");
        assertThat(rsp.getStatusCode()).isEqualTo(StaplerResponse.SC_NO_CONTENT);
        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b));
        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
      }
    });
  }

  @Test
  public void waitForStatusCancelPipeline() {
    story.addStep(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        QueueTaskFuture<WorkflowRun> pipeline = submitPipeline(JOB_NAME1, "TEST-1", "status", "'In Progress'");
        WorkflowRun b = pipeline.waitForStart();
        waitForStepToWait(1, b);
        b.doStop();
        story.j.assertBuildStatus(Result.ABORTED, pipeline);
        assertThat(JiraWebHook.get().listeners.get("TEST-1")).isNull();
      }
    });
  }

  private JSONWebResponse submitWebHook(String issueKey, String field, String toString)
      throws InterruptedException, IOException, SAXException {

    return story.j.postJSON(
        "jira-steps-webhook/notify",
        "{\n"
            + "\"webhookEvent\":\"jira:issue_updated\",\n"
            + "\"issue\": "
            + "{ \"key\":"
            + "\""
            + issueKey
            + "\""
            + "},"
            + "\"changelog\":{"
            + "\"items\":["
            + "{ \"field\":"
            + "\""
            + field
            + "\","
            + "\"toString\":\""
            + toString
            + "\""
            + "}]}"
            + "}");
  }

  private void waitForStepToWait(int expectedListeners, WorkflowRun b) throws InterruptedException {
    // Wait for the step to register to the webhook listener
    while (JiraWebHook.get().listeners.size() != expectedListeners && b.isBuilding()) {
      Thread.sleep(500);
    }
  }

  private QueueTaskFuture<WorkflowRun> submitPipeline(String jobName, String issueKey, String field, String values)
      throws IOException, InterruptedException, ExecutionException {
    JiraWebHook.get().listeners.clear();
    WorkflowJob p = story.j.jenkins.createProject(WorkflowJob.class, jobName);
    p.setDefinition(
        new CpsFlowDefinition(
            "jiraWaitForIssueUpdate field: '" + field + "', fieldValues: [" + values + "], idOrKey: '" + issueKey + "'",
            true));
    return p.scheduleBuild2(0);
  }
}
