package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for GetServerInfoStep class.
 *
 * @author Stuart Rowe
 */
public class GetServerInfoStepTest extends BaseTest {

  private GetServerInfoStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {
    final ResponseDataBuilder<Map<String, Object>> builder = ResponseData.builder();
    when(jiraServiceMock.getServerInfo())
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulGetServerInfo() throws Exception {
    final GetServerInfoStep step = new GetServerInfoStep();
    stepExecution = new GetServerInfoStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).getServerInfo();
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}