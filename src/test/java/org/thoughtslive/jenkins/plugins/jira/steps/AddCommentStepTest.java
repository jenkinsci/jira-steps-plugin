package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import hudson.AbortException;
import hudson.model.Cause.UserIdCause;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

/**
 * Unit test cases for AddCommentStep class.
 *
 * @author Naresh Rayapati
 */
public class AddCommentStepTest extends BaseTest {

  private AddCommentStep.Execution stepExecution;

  @BeforeEach
  public void setup() {
    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.addComment(anyString(), any()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
    when(jiraServiceMock.addComment(anyString(), anyString()))
        .thenReturn(builder.successful(true).code(200).message("Success").build());
  }

  @Test
  public void testDeprecatedWithEmptyIdOrKeyThrowsAbortException() throws Exception {

    final AddCommentStep step = new AddCommentStep("", "test comment");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testDeprecatedWithEmptyCommentThrowsAbortException() throws Exception {
    final AddCommentStep step = new AddCommentStep("TEST-1", "");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute and assert Test.
    assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
          stepExecution.run();
        }).withMessage("comment is empty.").withStackTraceContaining("AbortException")
        .withNoCause();
  }

  @Test
  public void testDeprecatedSuccessfulAddComment() throws Exception {
    final AddCommentStep step = new AddCommentStep("TEST-1", "test comment");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    // Execute Test.
    stepExecution.run();
    // Assert Test
    String comment = "test comment\n{panel}Automatically created by: [~anonymous] from [Build URL|http://localhost:8080/jira-testing/job/01]{panel}";
    verify(jiraServiceMock, times(1))
        .addComment("TEST-1", ImmutableMap.builder().put("body", comment).build());
    assertThat(step.isFailOnError()).isEqualTo(true);
  }

  private static final String PANEL_END =
      " from [Build URL|http://localhost:8080/jira-testing/job/01]{panel}";

  private void buildStartedBy(final String userId) {
    final UserIdCause cause = mock(UserIdCause.class);
    when(cause.getUserId()).thenReturn(userId);
    when(runMock.getCauses()).thenReturn(Collections.singletonList(cause));
  }

  private void jiraUsersFound(final Map<?, ?>... users) {
    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    when(jiraServiceMock.userSearchByQuery("jdoe", 2))
        .thenReturn(builder.successful(true).code(200).data(Arrays.asList(users)).build());
  }

  @Test
  public void testDeprecatedAddCommentMentionsJiraCloudAccountId() throws Exception {
    buildStartedBy("jdoe");
    jiraUsersFound(Map.of("accountId", "557058:abc123"));
    final AddCommentStep step = new AddCommentStep("TEST-1", "test comment");
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    stepExecution.run();

    String comment = "test comment\n{panel}Automatically created by: [~accountid:557058:abc123]"
        + PANEL_END;
    verify(jiraServiceMock, times(1))
        .addComment("TEST-1", ImmutableMap.builder().put("body", comment).build());
  }

  @Test
  public void testInputAddCommentMentionsJiraCloudAccountId() throws Exception {
    buildStartedBy("jdoe");
    jiraUsersFound(Map.of("accountId", "557058:abc123"));
    final AddCommentStep step = spy(new AddCommentStep("TEST-1", null));
    doReturn(new HashMap<>(Map.of("body", "test comment"))).when(step).getInput();
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    stepExecution.run();

    String comment = "test comment\n{panel}Automatically created by: [~accountid:557058:abc123]"
        + PANEL_END;
    verify(jiraServiceMock, times(1)).addComment("TEST-1", Map.of("body", comment));
  }

  @Test
  public void testInputAddCommentWithoutJiraUserIsPlainText() throws Exception {
    buildStartedBy("jdoe");
    jiraUsersFound();
    final AddCommentStep step = spy(new AddCommentStep("TEST-1", null));
    doReturn(new HashMap<>(Map.of("body", "test comment"))).when(step).getInput();
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    stepExecution.run();

    String comment = "test comment\n{panel}Automatically created by: jdoe" + PANEL_END;
    verify(jiraServiceMock, times(1)).addComment("TEST-1", Map.of("body", comment));
  }

  @Test
  public void testDeprecatedAddCommentWithoutAuditLogSkipsLookup() throws Exception {
    buildStartedBy("jdoe");
    final AddCommentStep step = spy(new AddCommentStep("TEST-1", "test comment"));
    doReturn(false).when(step).isAuditLog();
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    stepExecution.run();

    verify(jiraServiceMock, times(1))
        .addComment("TEST-1", ImmutableMap.builder().put("body", "test comment").build());
    verify(jiraServiceMock, never()).userSearchByQuery(any(), anyInt());
  }

  @Test
  public void testInputAddCommentWithoutAuditLogSkipsLookup() throws Exception {
    buildStartedBy("jdoe");
    final AddCommentStep step = spy(new AddCommentStep("TEST-1", null));
    doReturn(new HashMap<>(Map.of("body", "test comment"))).when(step).getInput();
    doReturn(false).when(step).isAuditLog();
    stepExecution = new AddCommentStep.Execution(step, contextMock);

    stepExecution.run();

    verify(jiraServiceMock, times(1)).addComment("TEST-1", Map.of("body", "test comment"));
    verify(jiraServiceMock, never()).userSearchByQuery(any(), anyInt());
  }
}
