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
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for EditIssueStep class.
 *
 * @author Naresh Rayapati
 */
public class EditIssueStepTest extends BaseTest {

  final Object issue = Maps.newHashMap(ImmutableMap.builder().put("fields",
      ImmutableMap.builder().put("description", "TEST")
          .put("summary", "TEST")
          .put("project", ImmutableMap.builder().put("id", "1000").build())
          .put("issueype", ImmutableMap.builder().put("id", "10000").build()).build()).build());

  EditIssueStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.updateIssue(anyString(), any(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());

  }

  @Test
  public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
    final EditIssueStep step = new EditIssueStep("", issue);
    stepExecution = new EditIssueStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testSuccessfulUpdateIssue() throws Exception {
    final EditIssueStep step = new EditIssueStep("TEST-1", issue);
    stepExecution = new EditIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).updateIssue("TEST-1", issue, new HashMap<>());
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
