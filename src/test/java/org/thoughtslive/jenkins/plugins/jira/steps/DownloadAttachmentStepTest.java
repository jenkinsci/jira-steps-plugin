package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import hudson.AbortException;
import java.io.IOException;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

/**
 * Unit test cases for DownloadAttachmentStep class.
 *
 * @author Naresh Rayapati
 */
public class DownloadAttachmentStepTest extends BaseTest {

  DownloadAttachmentStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseData.ResponseDataBuilder<Object> builder1 = ResponseData.builder();
    when(jiraServiceMock.getAttachment(anyString())).thenReturn(
        builder1.successful(true).code(200).message("Success").data(ImmutableMap.builder()
            .put("content", "https://localhost/secure/attachment/10000/test.txt").build()).build());
    final ResponseData.ResponseDataBuilder<ResponseBody> builder2 = ResponseData.builder();
    // By setting successful to false we are not really writing file to disk currently.
    when(jiraServiceMock.downloadAttachment(anyString())).thenReturn(
        builder2.successful(false).code(200).data(mock(ResponseBody.class)).message("Success")
            .build());
  }

  @Test
  public void testEmptyAttachmentIdThrowsAbortException() throws Exception {
    final DownloadAttachmentStep step = new DownloadAttachmentStep("", "test.txt", true);
    stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is null or empty").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testEmptyfileThrowsAbortException() throws Exception {
    final DownloadAttachmentStep step = new DownloadAttachmentStep("100000", "", false);
    stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("file is null or empty").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulDownloadAttachmentStep() throws Exception {
    final DownloadAttachmentStep step = new DownloadAttachmentStep("10000", "test.txt", true);
    stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1))
        .downloadAttachment("https://localhost/secure/attachment/10000/test.txt");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}