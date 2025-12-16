package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;

/**
 * Unit test cases for NewIssueStep class.
 *
 * @author Naresh Rayapati
 */
public class NewIssueStepTest extends BaseTest {

  NewIssueStep.Execution stepExecution;
  IssueInput issue = null;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("summary", "Summary");
    fields.put("description", null);
    fields.put("duedate", DateTime.now().toString());
    fields.put("project", ImmutableMap.builder().put("key", "TEST").build());
    fields.put("issuetype", ImmutableMap.builder().put("name", "Task").build());

    issue = IssueInput.builder().fields(fields).build();

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.createIssue(any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulNewIssue() throws Exception {
    final NewIssueStep step = new NewIssueStep(issue);
    stepExecution = new NewIssueStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).createIssue(issue);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
