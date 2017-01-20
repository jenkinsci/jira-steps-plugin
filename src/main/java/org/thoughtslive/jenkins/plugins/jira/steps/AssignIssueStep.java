package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to assign a JIRA Issue to given user.
 *
 * @author Naresh Rayapati
 */
public class AssignIssueStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final String userName;

	@DataBoundConstructor
	public AssignIssueStep(final String idOrKey, final String userName) {
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
			return "jiraAssignIssue";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Assign Issue";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Issue>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient AssignIssueStep step;

		@Override
		protected ResponseData<Issue> run() throws Exception {

			ResponseData<Issue> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Assigning issue: " + step.getIdOrKey() + " to: "
						+ step.getUserName());
				response = jiraService.assignIssue(step.getIdOrKey(), step.getUserName());
			}

			return logResponse(response);
		}
	}
}
