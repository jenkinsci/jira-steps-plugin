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
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to create a new JIRA Version.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewVersionStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final Version version;

	@DataBoundConstructor
	public NewVersionStep(final Version version) {
		this.version = version;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraNewVersion";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Create New Version";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Version>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient NewVersionStep step;

		@Override
		protected ResponseData<Version> run() throws Exception {

			ResponseData<Version> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Creating new version: " + step.getVersion());
				final String description = addMeta(step.getVersion().getDescription());
				step.getVersion().setDescription(description);
				response = jiraService.createVersion(step.getVersion());
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			//TODO Add validation - Or change the input type here ?
			return verifyCommon(step, listener, envVars, run);
		}
	}
}
