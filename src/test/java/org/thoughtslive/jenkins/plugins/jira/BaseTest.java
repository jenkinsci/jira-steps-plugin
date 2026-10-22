package org.thoughtslive.jenkins.plugins.jira;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

public class BaseTest {

  @Mock
  protected JiraService jiraServiceMock;
  @Mock
  protected StepContext contextMock;
  @Mock
  TaskListener taskListenerMock;
  @Mock
  protected Run<?, ?> runMock;
  @Mock
  EnvVars envVarsMock;
  @Mock
  PrintStream printStreamMock;
  @Mock
  Site mockSite;
  private AutoCloseable closeable;
  private MockedStatic<Site> site;

  @BeforeEach
  public void setUpBase() throws IOException, InterruptedException {
    closeable = MockitoAnnotations.openMocks(this);
    when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
    when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");

    when(mockSite.getService()).thenReturn(jiraServiceMock);
    // As on Jira Server / Data Center without query support: the audit log keeps [~buildUserId].
    final ResponseData.ResponseDataBuilder<Object> userSearch = ResponseData.builder();
    when(jiraServiceMock.userSearchByQuery(any(), anyInt()))
        .thenReturn(userSearch.successful(false).code(400).build());
    site = Mockito.mockStatic(Site.class);
    site.when(() -> Site.get(any())).thenReturn(mockSite);

    when(runMock.getCauses()).thenReturn(null);
    when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
    doNothing().when(printStreamMock).println();

    when(contextMock.get(Run.class)).thenReturn(runMock);
    when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
    when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
    when(contextMock.get(FilePath.class)).thenReturn(new FilePath(new File("test.txt")));
  }

  @AfterEach
  public void tearUpBase() throws Exception {
    site.close();
    closeable.close();
  }
}
