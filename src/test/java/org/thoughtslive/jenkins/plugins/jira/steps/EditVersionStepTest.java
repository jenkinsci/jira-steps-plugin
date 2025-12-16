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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

/**
 * Unit test cases for EditVersionStep class.
 *
 * @author Naresh Rayapati
 */
public class EditVersionStepTest extends BaseTest {

  EditVersionStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.updateVersion(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());

  }

  @Test
  public void testSuccessfulEditVersion() throws Exception {
    final Object version =
        Maps.newHashMap(ImmutableMap.builder().put("name", "testVersion").put("id", "10000")
            .put("project", "TEST").build());
    final EditVersionStep step = new EditVersionStep("10000", version);
    stepExecution = new EditVersionStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).updateVersion("10000", version);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
