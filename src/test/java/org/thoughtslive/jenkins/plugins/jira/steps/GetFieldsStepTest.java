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
 * Unit test cases for GetFieldsStep class.
 *
 * @author Naresh Rayapati
 */
public class GetFieldsStepTest extends BaseTest {

  GetFieldsStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getFields())
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulGetFieldsStep() throws Exception {
    final GetFieldsStep step = new GetFieldsStep();
    stepExecution = new GetFieldsStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getFields();
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
