package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Unit test cases for GetComponentStep class.
 * 
 * @author Naresh Rayapati
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GetComponentStep.class })
public class GetComponentStepTest {

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
	
	GetComponentStep.Execution stepExecution;

	@Before
	public void setup() {
		stepExecution = spy(new GetComponentStep.Execution());

		when(runMock.getCauses()).thenReturn(null);
		when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
		doNothing().when(printStreamMock).println();

		final ResponseDataBuilder<Component> builder = ResponseData.builder();
		when(jiraServiceMock.getComponent(anyInt()))
				.thenReturn(builder.successful(true).code(200).message("Success").build());

		when(envVarsMock.get("JIRA_URL")).thenReturn("http://localhost:8080/");
		when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");

		stepExecution.listener = taskListenerMock;
		stepExecution.envVars = envVarsMock;
		stepExecution.run = runMock;

		doReturn(jiraServiceMock).when(stepExecution).getJiraService(any());
	}
}