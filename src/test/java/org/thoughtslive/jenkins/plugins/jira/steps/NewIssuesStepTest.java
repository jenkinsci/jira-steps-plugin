package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssuesInput;

/**
 * Unit test cases for NewComponentStep class.
 *
 * @author Naresh Rayapati
 */
public class NewIssuesStepTest extends BaseTest {

  NewIssuesStep.Execution stepExecution;
  IssuesInput issuesInput;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    // Prepare site.
    final List<IssueInput> issues = new ArrayList<IssueInput>();
    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("summary", "Summary");
    fields.put("description", null);
    fields.put("duedate", DateTime.now().toString());
    fields.put("project", ImmutableMap.builder().put("key", "TEST").build());
    fields.put("issuetype", ImmutableMap.builder().put("name", "Task").build());

    final IssueInput issue = IssueInput.builder().fields(fields).build();
    issues.add(issue);

    issuesInput = IssuesInput.builder().issueUpdates(issues).build();

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.createIssues(any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulNewIssues() throws Exception {
    final NewIssuesStep step = new NewIssuesStep(issuesInput);
    stepExecution = new NewIssuesStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).createIssues(issuesInput);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
