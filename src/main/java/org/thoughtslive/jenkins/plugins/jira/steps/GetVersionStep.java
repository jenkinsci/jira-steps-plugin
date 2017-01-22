package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Version;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Version.
 *
 * @author Naresh Rayapati
 */
public class GetVersionStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;
	
	@Getter
	private final int id;

	@DataBoundConstructor
	public GetVersionStep(final int id) {
		this.id = id;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetVersion";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Version";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Version>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetVersionStep step;

		@Override
		protected ResponseData<Version> run() throws Exception {

			ResponseData<Version> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying Project Version with id:" + step.getId());
				response = jiraService.getVersion(step.getId());
			}

			return logResponse(response);
		}
	}
}
