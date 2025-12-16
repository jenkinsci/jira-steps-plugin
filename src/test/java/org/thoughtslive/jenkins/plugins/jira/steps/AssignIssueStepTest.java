package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
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
 * Unit test cases for AssignIssueStep class.
 *
 * @author Naresh Rayapati
 */
public class AssignIssueStepTest extends BaseTest {

  private AssignIssueStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.assignIssue(any(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final AssignIssueStep step = new AssignIssueStep("", "testUser", "testUserId");
    stepExecution = new AssignIssueStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithEmptyUserNameOrAccountIdThrowsAbortException() throws Exception {
    final AssignIssueStep step = new AssignIssueStep("TEST-1", "", "");
    stepExecution = new AssignIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).assignIssue("TEST-1", null);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }

  @Test
  public void testSuccessfulUnassignIssue() throws Exception {
    final AssignIssueStep step = new AssignIssueStep("TEST-1", null, null);
    stepExecution = new AssignIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).assignIssue("TEST-1", null);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }

  @Test
  public void testSuccessfulAssignIssue() throws Exception {
    final AssignIssueStep step = new AssignIssueStep("TEST-1", "testUser", null);
    stepExecution = new AssignIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).assignIssue("TEST-1", "testUser");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }

  @Test
  public void testSuccessfulAssignIssueByAccountId() throws Exception {
    final AssignIssueStep step = new AssignIssueStep("TEST-1", null, "testUser");
    stepExecution = new AssignIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).assignIssue("TEST-1", "testUser");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
