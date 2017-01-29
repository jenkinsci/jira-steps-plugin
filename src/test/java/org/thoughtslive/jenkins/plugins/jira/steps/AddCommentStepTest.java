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
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Unit test cases for AddCommentStep class.
 * 
 * @author Naresh Rayapati
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddCommentStep.class, Site.class})
public class AddCommentStepTest {

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

	AddCommentStep.Execution stepExecution;

	@Before
	public void setup() {

		// Prepare site.
		when(envVarsMock.get("JIRA_SITE")).thenReturn("LOCAL");
		when(envVarsMock.get("BUILD_URL")).thenReturn("http://localhost:8080/jira-testing/job/01");

		PowerMockito.mockStatic(Site.class);
		Mockito.when(Site.get(any())).thenReturn(siteMock);
		when(siteMock.getService()).thenReturn(jiraServiceMock);

		stepExecution = spy(new AddCommentStep.Execution());

		when(runMock.getCauses()).thenReturn(null);
		when(taskListenerMock.getLogger()).thenReturn(printStreamMock);
		doNothing().when(printStreamMock).println();

		final ResponseDataBuilder<Comment> builder = ResponseData.builder();
		when(jiraServiceMock.addComment(anyString(), anyString())).thenReturn(builder.successful(true).code(200).message("Success").build());

		stepExecution.listener = taskListenerMock;
		stepExecution.envVars = envVarsMock;
		stepExecution.run = runMock;

		doReturn(jiraServiceMock).when(stepExecution).getJiraService(any());
	}

	@Test
	public void testWithEmptyIdOrKeyThrowsAbortException() throws Exception {
		final AddCommentStep step = new AddCommentStep("", "test comment");
		stepExecution.step = step;

		// Execute and assert Test.
		assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
			stepExecution.run();
		}).withMessage("idOrKey is empty or null.").withStackTraceContaining("AbortException").withNoCause();
	}

	@Test
	public void testWithEmptyCommentThrowsAbortException() throws Exception {
		final AddCommentStep step = new AddCommentStep("TEST-1", "");
		stepExecution.step = step;

		// Execute and assert Test.
		assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
			stepExecution.run();
		}).withMessage("comment is empty or null.").withStackTraceContaining("AbortException").withNoCause();
	}

	@Test
	public void testSuccessfulAddComment() throws Exception {
		final AddCommentStep step = new AddCommentStep("TEST-1", "test comment");
		stepExecution.step = step;

		// Execute Test.
		stepExecution.run();

		// Assert Test
		verify(jiraServiceMock, times(1)).addComment("TEST-1",
				"test comment\n{panel}Automatically created by: [~anonymous] from [Build URL|http://localhost:8080/jira-testing/job/01]{panel}");
		assertThat(stepExecution.step.isFailOnError()).isEqualTo(true);
	}
}