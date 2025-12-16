package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.AbortException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for DeleteAttachmentStep class.
 *
 * @author Naresh Rayapati
 */
public class DeleteAttachmentStepTest extends BaseTest {

  DeleteAttachmentStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.deleteAttachment(anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdThrowsAbortException() throws Exception {
    final DeleteAttachmentStep step = new DeleteAttachmentStep("");
    stepExecution = new DeleteAttachmentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testWithNullIdThrowsAbortException() throws Exception {
    final DeleteAttachmentStep step = new DeleteAttachmentStep(null);
    stepExecution = new DeleteAttachmentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testSuccessfulDeleteAttachmentStep() throws Exception {
    final DeleteAttachmentStep step = new DeleteAttachmentStep("1000");
    stepExecution = new DeleteAttachmentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).deleteAttachment("1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
