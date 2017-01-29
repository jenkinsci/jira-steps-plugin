package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

import java.io.PrintStream;

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
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.api.input.FieldsInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Unit test cases for EditIssueStep class.
 * 
 * @author Naresh Rayapati
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EditIssueStep.class, Site.class })
public class EditIssueStepTest {

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
	
	EditIssueStep.Execution stepExecution;

	final IssueInput issue = IssueInput.builder().fields(FieldsInput.builder().description("TEST").summary("TEST").build()).build();

	@Before
	public void setup() {

		// Prepare site.
		when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
		when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");
		
		PowerMockito.mockStatic(Site.class);
		Mockito.when(Site.get(any())).thenReturn(siteMock);
		when(siteMock.getService()).thenReturn(jiraServiceMock);
		
		stepExecution = spy(new EditIssueStep.Execution());

		when(runMock.getCauses()).thenReturn(null);
		when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
		doNothing().when(printStreamMock).println();

		final ResponseDataBuilder<BasicIssue> builder = ResponseData.builder();
		when(jiraServiceMock.updateIssue(anyString(), any()))
				.thenReturn(builder.successful(true).code(200).message("Success").build());

		stepExecution.listener = taskListenerMock;
		stepExecution.envVars = envVarsMock;
		stepExecution.run = runMock;

		doReturn(jiraServiceMock).when(stepExecution).getJiraService(any());
	}

	@Test
	public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
		final EditIssueStep step = new EditIssueStep("", issue);
		stepExecution.step = step;

		
		// Execute and assert Test.
		assertThatExceptionOfType(AbortException.class)
			.isThrownBy(() -> { stepExecution.run(); })
			.withMessage("idOrKey is empty or null.")
			.withStackTraceContaining("AbortException")
			.withNoCause();
	}

	@Test
	public void testSuccessfulUpdateIssue() throws Exception {
		final EditIssueStep step = new EditIssueStep("TEST-1", issue);
		stepExecution.step = step;

		// Execute Test.
		stepExecution.run();

		// Assert Test
		verify(jiraServiceMock, times(1)).updateIssue("TEST-1", issue);
		assertThat(stepExecution.step.isFailOnError()).isEqualTo(true);
	}
}