package com.apwide.jenkins.steps;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import hudson.EnvVars;
import hudson.model.TaskListener;
import hudson.model.Run;

import java.io.IOException;
import java.io.PrintStream;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtslive.jenkins.plugins.jira.Site;

import com.apwide.jenkins.service.ApwideService;
import com.apwide.jenkins.util.ApwideSite;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Site.class, ApwideSite.class })
public abstract class BaseStepTest {

    @Mock
    protected TaskListener taskListenerMock;
    @Mock
    protected Run<?, ?> runMock;
    @Mock
    protected EnvVars envVarsMock;
    @Mock
    protected PrintStream printStreamMock;
    @Mock
    protected ApwideService apwideServiceMock;
    @Mock
    protected Site siteMock;
    @Mock
    protected ApwideSite apwideSiteMock;
    @Mock
    protected StepContext contextMock;

    @Before
    public void setup() throws IOException, InterruptedException {

	// Prepare site.
	when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
	when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");

	PowerMockito.mockStatic(Site.class);
	Mockito.when(Site.get("LOCAL")).thenReturn(siteMock);
	PowerMockito.mockStatic(ApwideSite.class);
	Mockito.when(ApwideSite.get("LOCAL")).thenReturn(apwideSiteMock);
	when(apwideSiteMock.getApwideService()).thenReturn(apwideServiceMock);

	when(runMock.getCauses()).thenReturn(null);
	when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
	doNothing().when(printStreamMock).println();

	when(contextMock.get(Run.class)).thenReturn(runMock);
	when(contextMock.get(TaskListener.class)).thenReturn(taskListenerMock);
	when(contextMock.get(EnvVars.class)).thenReturn(envVarsMock);
    }

    protected void setFailOnError(boolean failOnError) {
	when(envVarsMock.get("JIRA_FAIL_ON_ERROR")).thenReturn(failOnError + "");
    }

}
