package org.thoughtslive.jenkins.plugins.jira.steps;

import com.google.common.collect.ImmutableMap;
import hudson.AbortException;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import okhttp3.ResponseBody;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test cases for DownloadAttachmentStep class.
 *
 * @author Naresh Rayapati
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DownloadAttachmentStep.class, Site.class})
public class DownloadAttachmentStepTest {

    @Mock
    TaskListener taskListenerMock;
    @Mock
    Run<?, ?> runMock;
    @Mock
    EnvVars envVarsMock;
    @Mock
    PrintStream printStreamMock;
    @Mock
    JiraService jiraServiceMock;
    @Mock
    Site siteMock;
    @Mock
    StepContext contextMock;

    DownloadAttachmentStep.Execution stepExecution;

    @Before
    public void setup() throws IOException, InterruptedException {

        // Prepare site.
        when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
        when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");
        PowerMockito.mockStatic(Site.class);
        Mockito.when(Site.get(any())).thenReturn(siteMock);
        when(siteMock.getService()).thenReturn(jiraServiceMock);
        when(runMock.getCauses()).thenReturn(null);
        when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
        doNothing().when(printStreamMock).println();

        final ResponseData.ResponseDataBuilder<Object> builder1 = ResponseData.builder();
        when(jiraServiceMock.getAttachment(anyString())).thenReturn(builder1.successful(true).code(200).message("Success").data(ImmutableMap.builder().put("content", "https://localhost/secure/attachment/10000/test.txt").build()).build());
        final ResponseData.ResponseDataBuilder<ResponseBody> builder2 = ResponseData.builder();
        // By setting successful to false we are not really writing file to disk currently.
        when(jiraServiceMock.downloadAttachment(anyString())).thenReturn(builder2.successful(false).code(200).data(mock(ResponseBody.class)).message("Success").build());

        when(contextMock.get(Run.class)).thenReturn(runMock);
        when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
        when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
        when(contextMock.get(FilePath.class)).thenReturn(new FilePath(new File("test.txt")));
    }

    @Test
    public void testEmptyAttachmentIdThrowsAbortException() throws Exception {
        final DownloadAttachmentStep step = new DownloadAttachmentStep("", "test.txt", true);
        stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

        assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
            stepExecution.run();
        }).withMessage("id is null or empty").withStackTraceContaining("AbortException").withNoCause();
    }

    @Test
    public void testEmptyfileThrowsAbortException() throws Exception {
        final DownloadAttachmentStep step = new DownloadAttachmentStep("100000", "", false);
        stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

        assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
            stepExecution.run();
        }).withMessage("file is null or empty").withStackTraceContaining("AbortException").withNoCause();
    }

    @Test
    public void testSuccessfulDownloadAttachmentStep() throws Exception {
        final DownloadAttachmentStep step = new DownloadAttachmentStep("10000", "test.txt", true);
        stepExecution = new DownloadAttachmentStep.Execution(step, contextMock);

        // Execute Test.
        stepExecution.run();

        // Assert Test
        verify(jiraServiceMock, times(1)).downloadAttachment("https://localhost/secure/attachment/10000/test.txt");
        assertThat(step.isFailOnError()).isEqualTo(true);
    }
}