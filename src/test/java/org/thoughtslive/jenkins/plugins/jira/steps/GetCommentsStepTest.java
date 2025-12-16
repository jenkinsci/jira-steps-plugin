package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.AbortException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for GetCommentsStep class.
 *
 * @author Naresh Rayapati
 */
public class GetCommentsStepTest extends BaseTest {

  private GetCommentsStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getComments(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final GetCommentsStep step = new GetCommentsStep("");
    stepExecution = new GetCommentsStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulGetComments() throws Exception {
    final GetCommentsStep step = new GetCommentsStep("TEST-1");
    stepExecution = new GetCommentsStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getComments("TEST-1", new HashMap<>());
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
