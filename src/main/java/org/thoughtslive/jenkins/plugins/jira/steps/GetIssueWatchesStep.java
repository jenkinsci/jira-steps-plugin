package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Watches;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Issue watchers.
 *
 * @author Naresh Rayapati
 */
public class GetIssueWatchesStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@DataBoundConstructor
	public GetIssueWatchesStep(final String idOrKey) {
		this.idOrKey = idOrKey;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetIssueWatches";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Issue Watches";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Watches>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetIssueWatchesStep step;

		@Override
		protected ResponseData<Watches> run() throws Exception {

			ResponseData<Watches> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying issue watches - idOrKey: " + step.getIdOrKey());
				response = jiraService.getIssueWatches(step.getIdOrKey());
			}

			return logResponse(response);
		}
	}
}
