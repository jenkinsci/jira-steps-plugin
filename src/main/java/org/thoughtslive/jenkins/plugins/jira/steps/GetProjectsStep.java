package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;

/**
 * Step to query a JIRA Projects.
 *
 * @author Naresh Rayapati
 */
public class GetProjectsStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

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

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetProjectsStep step;

		@Override
		protected ResponseData<Project[]> run() throws Exception {

			ResponseData<Project[]> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying All Projects");
				response = jiraService.getProjects();
			}

			return logResponse(response);
		}
	}
}
