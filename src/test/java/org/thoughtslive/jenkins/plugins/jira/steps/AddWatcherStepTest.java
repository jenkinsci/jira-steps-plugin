package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
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
 * Unit test cases for AddWatcherStep class.
 *
 * @author Naresh Rayapati
 */
public class AddWatcherStepTest extends BaseTest {

  private AddWatcherStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.addIssueWatcher(anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final AddWatcherStep step = new AddWatcherStep("", "testUser");
    stepExecution = new AddWatcherStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("idOrKey is empty or null.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithEmptyCommentThrowsAbortException() throws Exception {
    final AddWatcherStep step = new AddWatcherStep("TEST-1", "");
    stepExecution = new AddWatcherStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("userName is empty or null.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulAddWatcher() throws Exception {
    final AddWatcherStep step = new AddWatcherStep("TEST-1", "testUser");
    stepExecution = new AddWatcherStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).addIssueWatcher("TEST-1", "testUser");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
