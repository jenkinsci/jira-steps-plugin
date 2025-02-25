package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import hudson.AbortException;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

/**
 * Unit test cases for AddCommentStep class.
 *
 * @author Naresh Rayapati
 */
public class AddCommentStepTest extends BaseTest {

  private AddCommentStep.Execution stepExecution;

  @Before
  public void setup() {
    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.addComment(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
    when(jiraServiceMock.addComment(anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testDeprecatedWithEmptyIdOrKeyThrowsAbortException() throws Exception {

    final AddCommentStep step = new AddCommentStep("", "test comment");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("idOrKey is empty or null.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testDeprecatedWithEmptyCommentThrowsAbortException() throws Exception {
    final AddCommentStep step = new AddCommentStep("TEST-1", "");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("comment is empty.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testDeprecatedSuccessfulAddComment() throws Exception {
    final AddCommentStep step = new AddCommentStep("TEST-1", "test comment");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();
    // Assert Test
    String comment = "test comment\n{panel}Automatically created by: [~anonymous] from [Build URL|http://localhost:8080/jira-testing/job/01]{panel}";
    verify(jiraServiceMock, times(1))
        .addComment("TEST-1", ImmutableMap.builder().put("body", comment).build());
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
