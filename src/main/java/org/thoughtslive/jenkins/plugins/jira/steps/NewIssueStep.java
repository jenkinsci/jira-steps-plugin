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
 * Step to create a new JIRA Issue.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewIssueStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final IssueInput issue;

	@DataBoundConstructor
	public NewIssueStep(final IssueInput issue) {
		this.issue = issue;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraNewIssue";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Create New Issue";
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
		private transient NewIssueStep step;

		@Override
		protected ResponseData<BasicIssue> run() throws Exception {

			ResponseData<BasicIssue> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Creating new issue: " + step.getIssue());
				final String description = step.getIssue().getFields().getDescription() + "\n Created by: \nBuild URL: " + buildUrl + "\nBuild User: [~" + buildUser +"]";
				step.getIssue().getFields().setDescription(description);
				response = jiraService.createIssue(step.getIssue());
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
