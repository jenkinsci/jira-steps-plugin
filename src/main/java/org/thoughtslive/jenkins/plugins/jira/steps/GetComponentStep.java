package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Component.
 *
 * @author Naresh Rayapati
 */
public class GetComponentStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final int id;

	@DataBoundConstructor
	public GetComponentStep(final int id) {
		this.id = id;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetComponent";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Component";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Component>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetComponentStep step;

		@Override
		protected ResponseData<Component> run() throws Exception {

			ResponseData<Component> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying component with id: " + step.getId());
				response = jiraService.getComponent(step.getId());
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
