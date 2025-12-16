package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
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
 * Unit test cases for AssignableUserSearchStep class.
 *
 * @author Naresh Rayapati
 */
public class AssignableUserSearchStepTest extends BaseTest {

  private AssignableUserSearchStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock
        .assignableUserSearch(any(), anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final AssignableUserSearchStep step = new AssignableUserSearchStep("", "");
    stepExecution = new AssignableUserSearchStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("either project or issueKey is required.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulJqlSearch() throws Exception {
    final AssignableUserSearchStep step = new AssignableUserSearchStep("TEST", "TEST-1");
    stepExecution = new AssignableUserSearchStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).assignableUserSearch(null, "TEST", "TEST-1", 0, 1000);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
