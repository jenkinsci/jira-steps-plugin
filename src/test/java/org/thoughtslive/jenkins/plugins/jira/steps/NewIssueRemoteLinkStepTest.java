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
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for NewIssueRemoteLinkStep class.
 *
 * @author Naresh Rayapati
 */
public class NewIssueRemoteLinkStepTest extends BaseTest {

  NewIssueRemoteLinkStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.createIssueRemoteLink(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithZeroIdThrowsAbortException() throws Exception {
    final NewIssueRemoteLinkStep step = new NewIssueRemoteLinkStep("", "");
    stepExecution = new NewIssueRemoteLinkStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulNewIssueRemoteLinksStep() throws Exception {
    final NewIssueRemoteLinkStep step = new NewIssueRemoteLinkStep("TEST-27", "");
    stepExecution = new NewIssueRemoteLinkStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).createIssueRemoteLink("TEST-27", "");
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
