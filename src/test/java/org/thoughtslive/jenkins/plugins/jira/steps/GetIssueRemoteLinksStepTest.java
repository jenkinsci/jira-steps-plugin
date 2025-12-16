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
 * Unit test cases for GetIssueRemoteLinksStep class.
 *
 * @author Naresh Rayapati
 */
public class GetIssueRemoteLinksStepTest extends BaseTest {

  GetIssueRemoteLinksStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {
    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getIssueRemoteLinks(anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithZeroIdThrowsAbortException() throws Exception {
    final GetIssueRemoteLinksStep step = new GetIssueRemoteLinksStep("", "");
    stepExecution = new GetIssueRemoteLinksStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithNegativeIdThrowsAbortException() throws Exception {
    final GetIssueRemoteLinksStep step = new GetIssueRemoteLinksStep(null, "");
    stepExecution = new GetIssueRemoteLinksStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulGetIssueRemoteLinksStep() throws Exception {
    final GetIssueRemoteLinksStep step = new GetIssueRemoteLinksStep("TEST-27", "1000");
    stepExecution = new GetIssueRemoteLinksStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getIssueRemoteLinks("TEST-27", "1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
