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
 * Unit test cases for GetIssueLinkTypesStep class.
 *
 * @author Naresh Rayapati
 */
public class GetIssueLinkTypesStepTest extends BaseTest {

  GetIssueLinkTypesStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.getIssueLinkTypes())
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulGetIssueLinkTypesStep() throws Exception {
    final GetIssueLinkTypesStep step = new GetIssueLinkTypesStep();
    stepExecution = new GetIssueLinkTypesStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getIssueLinkTypes();
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
