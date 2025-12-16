package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for GetProjectsStep class.
 *
 * @author Naresh Rayapati
 */
public class GetProjectsStepTest extends BaseTest {

  GetProjectsStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getProjects())
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulGetProjectsStep() throws Exception {
    final GetProjectsStep step = new GetProjectsStep();
    stepExecution = new GetProjectsStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getProjects();
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
