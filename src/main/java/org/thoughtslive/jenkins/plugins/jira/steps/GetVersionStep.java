package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

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
 * Step to query a JIRA Version.
 *
 * @author Naresh Rayapati
 */
public class GetVersionStep extends BasicJiraStep {

	private static final long serialVersionUID = -4252560961571411897L;
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

		private static final long serialVersionUID = 325576266548671174L;

		@StepContextParameter
		transient Run<?, ?> run;

		@StepContextParameter
		transient TaskListener listener;

		@StepContextParameter
		transient EnvVars envVars;

		@Inject
		transient GetVersionStep step;

		@Override
		protected ResponseData<Version> run() throws Exception {

			ResponseData<Version> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying Project Version with id:" + step.getId());
				response = jiraService.getVersion(step.getId());
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			String errorMessage = null;
			ResponseData<T> response = verifyCommon(step, listener, envVars, run);

			if (response == null) {
				if (step.getId() <= 0) {
					errorMessage = "id less than or equals to zero.";
				}

				if (errorMessage != null) {
					response = buildErrorResponse(new RuntimeException(errorMessage));
				}
			}
			return response;
		}
	}
}
