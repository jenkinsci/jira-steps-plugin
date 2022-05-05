package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
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
 * Unit test cases for NewVersionStep class.
 *
 * @author Naresh Rayapati
 */
public class NewVersionStepTest extends BaseTest {

  NewVersionStep.Execution stepExecution;

  @Before
  public void setup() throws IOException, InterruptedException {
    final ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.createVersion(any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulNewVersion() throws Exception {
    final Object version = Maps.newHashMap(
        ImmutableMap.builder().put("name", "testVersion").put("id", "10000").put("project", "TEST")
            .put("description", "hello").build());
    final NewVersionStep step = new NewVersionStep(version);
    stepExecution = new NewVersionStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).createVersion(version);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
