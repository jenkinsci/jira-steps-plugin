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
 * Unit test cases for GetComponentStep class.
 *
 * @author Naresh Rayapati
 */
public class GetComponentStepTest extends BaseTest {

  GetComponentStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getComponent(anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithZeroComponentIdThrowsAbortException() throws Exception {
    final GetComponentStep step = new GetComponentStep("");
    stepExecution = new GetComponentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testWithNegativeComponentIdThrowsAbortException() throws Exception {
    final GetComponentStep step = new GetComponentStep(null);
    stepExecution = new GetComponentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
      stepExecution.run();
    }).withMessage("id is empty or null.").withStackTraceContaining("AbortException").withNoCause();
  }

  @Test
  public void testSuccessfulGetComponent() throws Exception {
    final GetComponentStep step = new GetComponentStep("1000");
    stepExecution = new GetComponentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getComponent("1000");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
