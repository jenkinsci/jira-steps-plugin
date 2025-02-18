package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import hudson.AbortException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for NotifyIssueStep class.
 *
 * @author Naresh Rayapati
 */
public class NotifyIssueStepTest extends BaseTest {

  final Object notify = Maps.newHashMap(
      ImmutableMap.builder().put("subject", "TEST SUBJECT").put("textBody", "TEST BODY").build());
  NotifyIssueStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.notifyIssue(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final NotifyIssueStep step = new NotifyIssueStep("", notify);
    stepExecution = new NotifyIssueStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("idOrKey is empty or null.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulNotifyIssue() throws Exception {
    final NotifyIssueStep step = new NotifyIssueStep("TEST-1", notify);
    stepExecution = new NotifyIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).notifyIssue("TEST-1", notify);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
