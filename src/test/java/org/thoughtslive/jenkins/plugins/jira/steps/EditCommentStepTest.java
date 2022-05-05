package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import hudson.AbortException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for EditCommentStep class.
 *
 * @author Naresh Rayapati
 */
public class EditCommentStepTest extends BaseTest {

  EditCommentStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.updateComment(anyString(), anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
    when(jiraServiceMock.updateComment(anyString(), anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testDeprecatedWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final EditCommentStep step = new EditCommentStep("", "1000", "test comment");
    stepExecution = new EditCommentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testDeprecatedSuccessfulEditComment() throws Exception {
    final EditCommentStep step = new EditCommentStep("TEST-1", "1000", "test comment");
    stepExecution = new EditCommentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).updateComment("TEST-1", "1000",
        ImmutableMap.builder().put("body", "test comment").build());
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
