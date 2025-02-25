package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyInt;
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
 * Unit test cases for UserSearchStep class.
 *
 * @author Naresh Rayapati
 */
public class UserSearchStepTest extends BaseTest {

  private UserSearchStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.userSearch(anyString(), anyInt(), anyInt()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final UserSearchStep step = new UserSearchStep("");
    stepExecution = new UserSearchStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class)
        .isThrownBy(() -> stepExecution.run())
        .withMessage("queryStr is empty or null.")
        .withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulUserSearch() throws Exception {
    final UserSearchStep step = new UserSearchStep("jenkins");
    stepExecution = new UserSearchStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).userSearch("jenkins", 0, 1000);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
