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
 * Unit test cases for GetVersionStep class.
 *
 * @author Naresh Rayapati
 */
public class GetVersionStepTest extends BaseTest {

  GetVersionStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getVersion(anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithZeroIdThrowsAbortException() throws Exception {
    final GetVersionStep step = new GetVersionStep("");
    stepExecution = new GetVersionStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testWithNegativeIdThrowsAbortException() throws Exception {
    final GetVersionStep step = new GetVersionStep(null);
    stepExecution = new GetVersionStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testSuccessfulGetVersionStep() throws Exception {
    final GetVersionStep step = new GetVersionStep("1000");
    stepExecution = new GetVersionStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getVersion("1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
