package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to update given JIRA issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class EditIssueStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final IssueInput issue;

	@DataBoundConstructor
	public EditIssueStep(final String idOrKey, final IssueInput issue) {
		this.idOrKey = idOrKey;
		this.issue = issue;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraEditIssue";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Edit Issue";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<BasicIssue>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient EditIssueStep step;

		@Override
		protected ResponseData<BasicIssue> run() throws Exception {

			ResponseData<BasicIssue> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Updating issue: " + step.getIdOrKey());
				final String description = step.getIssue().getFields().getDescription() + "\n Created by: \nBuild URL: " + buildUrl + "\nBuild User: [~" + buildUser +"]";
				step.getIssue().getFields().setDescription(description);
				response = jiraService.updateIssue(step.getIdOrKey(), step.getIssue());
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
