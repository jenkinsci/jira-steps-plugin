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
 * Unit test cases for GetIssueTransitionsStep class.
 *
 * @author Naresh Rayapati
 */
public class GetIssueTransitionsStepTest extends BaseTest {

  private GetIssueTransitionsStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getIssueTransitions(anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());

  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final GetIssueTransitionsStep step = new GetIssueTransitionsStep("");
    stepExecution = new GetIssueTransitionsStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulGetIssueTransitionsStep() throws Exception {
    final GetIssueTransitionsStep step = new GetIssueTransitionsStep("TEST-1");
    stepExecution = new GetIssueTransitionsStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getIssueTransitions("TEST-1");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
