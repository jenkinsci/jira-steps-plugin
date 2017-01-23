package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Notify;
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
 * Step to notify issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class NotifyIssueStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final Notify notify;

	@DataBoundConstructor
	public NotifyIssueStep(final String idOrKey, final Notify notify) {
		this.idOrKey = idOrKey;
		this.notify = notify;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraNotifyIssue";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Notify Issue";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Void>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient NotifyIssueStep step;

		@Override
		protected ResponseData<Void> run() throws Exception {

			ResponseData<Void> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Notifing Issue: " + step.getIdOrKey());
				response = jiraService.notifyIssue(step.getIdOrKey(), step.getNotify());
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

				// TODO Validate Version object too.
				if (errorMessage != null) {
					response = buildErrorResponse(new RuntimeException(errorMessage));
				}
			}
			return response;
		}
	}
}
