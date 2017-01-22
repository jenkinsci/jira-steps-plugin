package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Project.
 *
 * @author Naresh Rayapati
 */
public class GetProjectStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;
	
	@Getter
	private final String idOrKey;

	@DataBoundConstructor
	public GetProjectStep(final String idOrKey) {
		this.idOrKey = idOrKey;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetProject";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Project";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Project>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetProjectStep step;

		@Override
		protected ResponseData<Project> run() throws Exception {

			ResponseData<Project> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying Project with idOrKey:" + step.getIdOrKey());
				response = jiraService.getProject(step.getIdOrKey());
			}

			return logResponse(response);
		}
	}
}
