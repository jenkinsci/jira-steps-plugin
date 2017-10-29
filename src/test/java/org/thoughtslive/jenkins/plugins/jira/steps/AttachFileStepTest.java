package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;
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
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Test class for the AttachFileStep.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AttachFileStep.class, Site.class})
public class AttachFileStepTest {

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

    AttachFileStep.Execution stepExecution;

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

        final ResponseData.ResponseDataBuilder<Object> builder = ResponseData.builder();
        when(jiraServiceMock.attachFile(anyString(), new File(anyString())))
                .thenReturn(builder.successful(true).code(200).message("Success").build());

        when(contextMock.get(Run.class)).thenReturn(runMock);
        when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
        when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
    }

    @Test
    public void testEmptyAttachmentPathThrowsAbortException() throws Exception {
        final AttachFileStep step = new AttachFileStep("TEST-27", "");
        stepExecution = new AttachFileStep.Execution(step, contextMock);

        assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
            stepExecution.run();
        }).withMessage("Attachment does not exist").withStackTraceContaining("AbortException").withNoCause();
    }

    @Test
    public void testEmptyIdThrowsAbortException() throws Exception {
        final AttachFileStep step = new AttachFileStep("", "Attachment does not exist");
        stepExecution = new AttachFileStep.Execution(step, contextMock);

        assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
            stepExecution.run();
        }).withMessage("ID or key is null or empty").withStackTraceContaining("AbortException").withNoCause();
    }

    @Test
    public void testSuccessfulDownloadAttachmentStep() throws Exception {
        final AttachFileStep step = new AttachFileStep("TEST-27",
                getClass().getResource("/org/thoughtslive/jenkins/plugins/jira/test/test.xlsx").getFile());
        stepExecution = new AttachFileStep.Execution(step, contextMock);

        // Execute Test.
        stepExecution.run();

        // Assert Test
        verify(jiraServiceMock, times(1)).attachFile("TEST-27",
                new File(getClass().getResource("/org/thoughtslive/jenkins/plugins/jira/test/test.xlsx").toURI()));
        assertThat(step.isFailOnError()).isEqualTo(true);
    }
}
