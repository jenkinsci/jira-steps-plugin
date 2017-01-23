package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
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
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetProjectStep step;

		@Override
		protected ResponseData<Project> run() throws Exception {

			ResponseData<Project> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying Project with idOrKey:" + step.getIdOrKey());
				response = jiraService.getProject(step.getIdOrKey());
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
