package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.Assume;
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
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

/**
 * Unit test cases for UploadAttachmentStep class.
 *
 * @author Naresh Rayapati
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UploadAttachmentStep.class, Site.class})
public class UploadAttachmentStepTest {

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
  StepContext contextMock;

  UploadAttachmentStep.Execution stepExecution;

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

    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    // By setting successful to false we are not really writing file to disk currently.
    when(jiraServiceMock.uploadAttachment(any(), any(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());

    when(contextMock.get(Run.class)).thenReturn(runMock);
    when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
    when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
    when(contextMock.get(FilePath.class)).thenReturn(new FilePath(new File("src/test/resources")));
  }

  @Test
  public void testEmptyIdOrKeyThrowsAbortException() throws Exception {
    final UploadAttachmentStep step = new UploadAttachmentStep("", "test.txt");
    stepExecution = new UploadAttachmentStep.Execution(step, contextMock);

    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("ID or key is null or empty").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testEmptyFileThrowsAbortException() throws Exception {
    final UploadAttachmentStep step = new UploadAttachmentStep("TEST-1", "");
    stepExecution = new UploadAttachmentStep.Execution(step, contextMock);

    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("file is null or empty").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulDownloadAttachmentStep() throws Exception {
    final UploadAttachmentStep step = new UploadAttachmentStep("TEST-1", "test.txt");
    stepExecution = new UploadAttachmentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).uploadAttachment("TEST-1", "src/test/resources/test.txt".replace('/', File.separatorChar),
        IOUtils.toByteArray(new FilePath(new File("src/test/resources/test.txt")).read()));
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}