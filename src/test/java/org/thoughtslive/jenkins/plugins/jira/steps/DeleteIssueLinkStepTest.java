package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.AbortException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for DeleteIssueLinkStep class.
 *
 * @author Naresh Rayapati
 */
public class DeleteIssueLinkStepTest extends BaseTest {

  DeleteIssueLinkStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.deleteIssueLink(anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdThrowsAbortException() throws Exception {
    final DeleteIssueLinkStep step = new DeleteIssueLinkStep("");
    stepExecution = new DeleteIssueLinkStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testWithNullIdThrowsAbortException() throws Exception {
    final DeleteIssueLinkStep step = new DeleteIssueLinkStep(null);
    stepExecution = new DeleteIssueLinkStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testSuccessfulDeleteIssueLinkStep() throws Exception {
    final DeleteIssueLinkStep step = new DeleteIssueLinkStep("1000");
    stepExecution = new DeleteIssueLinkStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).deleteIssueLink("1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
