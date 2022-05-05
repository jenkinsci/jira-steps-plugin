package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for TransitionIssueStep class.
 *
 * @author Naresh Rayapati
 */
public class TransitionIssueStepTest extends BaseTest {

  TransitionIssueStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.transitionIssue(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulTransitionIssue() throws Exception {
    final Object input = Maps.newHashMap(ImmutableMap.builder()
        .put("transition", ImmutableMap.builder().put("id", "1000").put("name", "TEST").build())
        .build());
    final TransitionIssueStep step = new TransitionIssueStep("TEST-1", input);
    stepExecution = new TransitionIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).transitionIssue("TEST-1", input);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
