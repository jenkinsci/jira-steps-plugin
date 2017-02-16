package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintStream;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.input.FieldsInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.TransitionInput;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Unit test cases for TransitionIssueStep class.
 * 
 * @author Naresh Rayapati
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransitionIssueStep.class, Site.class})
public class TransitionIssueStepTest {

  @Mock
  TaskListener taskListenerMock;
  @Mock
  Run<?, ?> runMock;
  @Mock
  EnvVars envVarsMock;
  @Mock
  PrintStream printStreamMock;
  @Mock
  JiraService jiraServiceMock;
  @Mock
  Site siteMock;
  @Mock
  TransitionInput transitionInputMock;
  @Mock
  StepContext contextMock;

  TransitionIssueStep.Execution stepExecution;

  final IssueInput issue = IssueInput.builder()
      .fields(FieldsInput.builder().description("TEST").summary("TEST").build()).build();

  @Before
  public void setup() throws IOException, InterruptedException {

    // Prepare site.
    when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
    when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");

    PowerMockito.mockStatic(Site.class);
    Mockito.when(Site.get(any())).thenReturn(siteMock);
    when(siteMock.getService()).thenReturn(jiraServiceMock);

    when(runMock.getCauses()).thenReturn(null);
    when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
    doNothing().when(printStreamMock).println();

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.transitionIssue(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());

    when(contextMock.get(Run.class)).thenReturn(runMock);
    when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
    when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
  }

  @Test
  public void testSuccessfulTransitionIssue() throws Exception {
    final TransitionIssueStep step = new TransitionIssueStep("TEST-1", transitionInputMock);
    stepExecution = new TransitionIssueStep.Execution(step, contextMock);;

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).transitionIssue("TEST-1", transitionInputMock);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
