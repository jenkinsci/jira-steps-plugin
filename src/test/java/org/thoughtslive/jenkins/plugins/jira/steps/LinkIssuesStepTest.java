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
 * Unit test cases for LinkIssuesStep class.
 *
 * @author Naresh Rayapati
 */
public class LinkIssuesStepTest extends BaseTest {

  private LinkIssuesStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.linkIssues(anyString(), anyString(), anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyTypeThrowsAbortException() throws Exception {
    final LinkIssuesStep step = new LinkIssuesStep("", "TEST-1", "TEST-2");
    stepExecution = new LinkIssuesStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("type is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithEmptyInwardKeyThrowsAbortException() throws Exception {
    final LinkIssuesStep step = new LinkIssuesStep("Relates", "", "TEST-2");
    stepExecution = new LinkIssuesStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("inwardKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testWithEmptyOutwardKeyThrowsAbortException() throws Exception {
    final LinkIssuesStep step = new LinkIssuesStep("Relates", "TEST-1", "");
    stepExecution = new LinkIssuesStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("outwardKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulLinkIssuesStep() throws Exception {
    final LinkIssuesStep step = new LinkIssuesStep("Relates", "TEST-1", "TEST-2");
    stepExecution = new LinkIssuesStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).linkIssues("Relates", "TEST-1", "TEST-2", null);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
