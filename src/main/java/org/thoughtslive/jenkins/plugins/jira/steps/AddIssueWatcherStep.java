package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to add watcher to issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class AddIssueWatcherStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final String userName;

	@DataBoundConstructor
	public AddIssueWatcherStep(final String idOrKey, final String userName) {
		this.idOrKey = idOrKey;
		this.userName = userName;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraAddIssueWatcher";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Add Issue Watcher";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Void>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient AddIssueWatcherStep step;

		@Override
		protected ResponseData<Void> run() throws Exception {

			ResponseData<Void> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Adding watcher "+ step.getUserName() +" to issue: " + step.getIdOrKey());
				response = jiraService.addIssueWatcher(step.getIdOrKey(), step.getUserName());
			}

			return logResponse(response);
		}
	}
}
