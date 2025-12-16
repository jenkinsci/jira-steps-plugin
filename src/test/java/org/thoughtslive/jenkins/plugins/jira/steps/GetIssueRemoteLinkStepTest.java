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
 * Unit test cases for GetIssueRemoteLinkStep class.
 *
 * @author Naresh Rayapati
 */
public class GetIssueRemoteLinkStepTest extends BaseTest {

  GetIssueRemoteLinkStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getIssueRemoteLink(anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithZeroIdThrowsAbortException() throws Exception {
    final GetIssueRemoteLinkStep step = new GetIssueRemoteLinkStep("", "10000");
    stepExecution = new GetIssueRemoteLinkStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithNegativeIdThrowsAbortException() throws Exception {
    final GetIssueRemoteLinkStep step = new GetIssueRemoteLinkStep("TEST-27", "");
    stepExecution = new GetIssueRemoteLinkStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("linkId is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulGetIssueRemoteLinkStep() throws Exception {
    final GetIssueRemoteLinkStep step = new GetIssueRemoteLinkStep("TEST-27", "1000");
    stepExecution = new GetIssueRemoteLinkStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getIssueRemoteLink("TEST-27", "1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
