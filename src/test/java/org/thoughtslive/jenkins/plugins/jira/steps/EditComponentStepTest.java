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
 * Unit test cases for EditComponentStep class.
 *
 * @author Naresh Rayapati
 */
public class EditComponentStepTest extends BaseTest {

  EditComponentStep.Execution stepExecution;

  @BeforeEach
  public void setup() throws IOException, InterruptedException {

    final ResponseDataBuilder<Void> builder = ResponseData.builder();
    when(jiraServiceMock.updateComponent(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testSuccessfulEditComponent() throws Exception {
    final Object component =
        Maps.newHashMap(ImmutableMap.builder().put("id", "1000").put("name", "testcomponent")
            .put("project", "TEST").build());
    final EditComponentStep step = new EditComponentStep("100", component);
    stepExecution = new EditComponentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();

    // Assert Test
    verify(jiraServiceMock, times(1)).updateComponent("100", component);
    assertThat(step.isFailOnError()).isEqualTo(true);
  }
}
