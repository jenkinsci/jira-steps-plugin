package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.AbortException;
import hudson.FilePath;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

/**
 * Unit test cases for UploadAttachmentStep class.
 *
 * @author Naresh Rayapati
 */
public class UploadAttachmentStepTest extends BaseTest {

  UploadAttachmentStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    // By setting successful to false we are not really writing file to disk currently.
    when(jiraServiceMock.uploadAttachment(any(), any(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
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
    verify(jiraServiceMock, times(1)).uploadAttachment("TEST-1",
        "src/test/resources/test.txt".replace('/', File.separatorChar),
        IOUtils.toByteArray(new FilePath(new File("src/test/resources/test.txt")).read()));
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}