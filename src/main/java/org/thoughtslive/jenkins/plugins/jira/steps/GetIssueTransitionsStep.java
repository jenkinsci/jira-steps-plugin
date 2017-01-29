package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Transitions;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Issue transitions.
 *
 * @author Naresh Rayapati
 */
public class GetIssueTransitionsStep extends BasicJiraStep {

	private static final long serialVersionUID = 76788852720885769L;

	@Getter
	private final String idOrKey;

	@DataBoundConstructor
	public GetIssueTransitionsStep(final String idOrKey) {
		this.idOrKey = idOrKey;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetIssueTransitions";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Issue Transitions";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Transitions>> {

		private static final long serialVersionUID = 4731872444274410275L;

		@StepContextParameter
		transient Run<?, ?> run;

		@StepContextParameter
		transient TaskListener listener;

		@StepContextParameter
		transient EnvVars envVars;

		@Inject
		transient GetIssueTransitionsStep step;

		@Override
		protected ResponseData<Transitions> run() throws Exception {

			ResponseData<Transitions> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying issue transitions with idOrKey: " + step.getIdOrKey());
				response = jiraService.getTransitions(step.getIdOrKey());
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			String errorMessage = null;
			ResponseData<T> response = verifyCommon(step, listener, envVars, run);

			if (response == null) {
				final String idOrKey = Util.fixEmpty(step.getIdOrKey());

				if (idOrKey == null) {
					errorMessage = "idOrKey is empty or null.";
				}

				if (errorMessage != null) {
					response = buildErrorResponse(new RuntimeException(errorMessage));
				}
			}
			return response;
		}
	}
}
