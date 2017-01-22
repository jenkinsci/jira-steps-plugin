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
 * Step to update given JIRA verion.
 * 
 * @author Naresh Rayapati
 *
 */
public class UpdateVersionStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final Version version;

	@DataBoundConstructor
	public UpdateVersionStep(final Version version) {
		this.version = version;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraUpdateVersion";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Update Version";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Version>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient UpdateVersionStep step;

		@Override
		protected ResponseData<Version> run() throws Exception {

			ResponseData<Version> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Updating version: " + step.getVersion());
				response = jiraService.updateVersion(step.getVersion());
			}

			return logResponse(response);
		}
	}
}
