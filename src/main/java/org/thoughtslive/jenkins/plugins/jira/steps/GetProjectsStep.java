package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Step to query a JIRA Projects.
 *
 * @author Naresh Rayapati
 */
public class GetProjectsStep extends BasicJiraStep {

	private static final long serialVersionUID = 2689031885988669114L;

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetProjects";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Projects";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Project[]>> {

		private static final long serialVersionUID = -5702548715847670073L;

		@StepContextParameter
		transient Run<?, ?> run;

		@StepContextParameter
		transient TaskListener listener;

		@StepContextParameter
		transient EnvVars envVars;

		@Inject
		transient GetProjectsStep step;

		@Override
		protected ResponseData<Project[]> run() throws Exception {

			ResponseData<Project[]> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying All Projects");
				response = jiraService.getProjects();
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			return verifyCommon(step, listener, envVars, run);
		}
	}
}
