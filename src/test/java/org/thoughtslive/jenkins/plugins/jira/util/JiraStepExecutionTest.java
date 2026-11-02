package org.thoughtslive.jenkins.plugins.jira.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserIdCause;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thoughtslive.jenkins.plugins.jira.BaseTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;

/**
 * Unit test cases for JiraStepExecution class.
 */
public class JiraStepExecutionTest extends BaseTest {

  private static final String PANEL_END =
      " from [Build URL|http://localhost:8080/jira-testing/job/01]{panel}";

  private JiraStepExecution<Void> stepExecution;

  @BeforeEach
  public void setup() throws Exception {
    stepExecution = new JiraStepExecution<Void>(contextMock) {
      @Override
      protected Void run() {
        return null;
      }

      @Override
      protected <T> ResponseData<T> verifyInput() {
        return null;
      }
    };
    stepExecution.jiraService = jiraServiceMock;
    stepExecution.buildUserId = "jdoe";
    stepExecution.buildUrl = "http://localhost:8080/jira-testing/job/01";
  }

  private static ResponseData<Object> users(final Map<?, ?>... users) {
    final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
    return builder.successful(true).code(200).data(Arrays.asList(users)).build();
  }

  @Test
  public void testPrepareBuildUserIdFromUserIdCause() {
    final UserIdCause cause = mock(UserIdCause.class);
    when(cause.getUserId()).thenReturn("jdoe");

    assertThat(JiraStepExecution.prepareBuildUserId(Collections.singletonList(cause)))
        .isEqualTo("jdoe");
  }

  @Test
  public void testPrepareBuildUserIdFromUpstreamCause() {
    final UserIdCause cause = mock(UserIdCause.class);
    when(cause.getUserId()).thenReturn("jdoe");
    final UpstreamCause upstream = mock(UpstreamCause.class);
    when(upstream.getUpstreamCauses()).thenReturn(Collections.singletonList(cause));

    assertThat(JiraStepExecution.prepareBuildUserId(Collections.singletonList(upstream)))
        .isEqualTo("jdoe");
  }

  @Test
  public void testPrepareBuildUserIdWithoutUserIsAnonymous() {
    assertThat(JiraStepExecution.prepareBuildUserId(null)).isEqualTo("anonymous");
    assertThat(JiraStepExecution.prepareBuildUserId(
        Collections.<Cause>singletonList(mock(Cause.class)))).isEqualTo("anonymous");
  }

  @Test
  public void testAddPanelMetaMentionsJiraCloudAccountId() {
    when(jiraServiceMock.userSearchByQuery("jdoe", 2))
        .thenReturn(users(Map.of("accountId", "557058:abc123", "displayName", "John Doe")));

    assertThat(stepExecution.addPanelMeta("message")).isEqualTo(
        "message\n{panel}Automatically created by: [~accountid:557058:abc123]" + PANEL_END);
  }

  @Test
  public void testAddPanelMetaMentionsJiraServerUsername() {
    when(jiraServiceMock.userSearchByQuery("jdoe", 2))
        .thenReturn(users(Map.of("name", "john.doe", "displayName", "John Doe")));

    assertThat(stepExecution.addPanelMeta("message"))
        .isEqualTo("message\n{panel}Automatically created by: [~john.doe]" + PANEL_END);
  }

  @Test
  public void testAddPanelMetaWithoutMatchIsPlainText() {
    when(jiraServiceMock.userSearchByQuery("jdoe", 2)).thenReturn(users());

    assertThat(stepExecution.addPanelMeta("message"))
        .isEqualTo("message\n{panel}Automatically created by: jdoe" + PANEL_END);
  }

  @Test
  public void testAddPanelMetaWithMultipleMatchesIsPlainText() {
    when(jiraServiceMock.userSearchByQuery("jdoe", 2)).thenReturn(
        users(Map.of("accountId", "557058:abc123"), Map.of("accountId", "557058:def456")));

    assertThat(stepExecution.addPanelMeta("message"))
        .isEqualTo("message\n{panel}Automatically created by: jdoe" + PANEL_END);
  }

  @Test
  public void testAddPanelMetaKeepsBuildUserMentionWhenSearchFails() {
    // BaseTest's search fails, as on Jira Server / Data Center without query support.
    assertThat(stepExecution.addPanelMeta("message"))
        .isEqualTo("message\n{panel}Automatically created by: [~jdoe]" + PANEL_END);
  }

  @Test
  public void testAddPanelMetaLooksUpBuildUserOncePerExecution() {
    when(jiraServiceMock.userSearchByQuery("jdoe", 2))
        .thenReturn(users(Map.of("accountId", "557058:abc123")));

    stepExecution.addPanelMeta("first");
    stepExecution.addPanelMeta("second");

    verify(jiraServiceMock, times(1)).userSearchByQuery("jdoe", 2);
  }
}
