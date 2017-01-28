package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssues;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssuesInput;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to create a new JIRA Issues.
 * 
 * @author Naresh Rayapati
 *
 */
public class NewIssuesStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final IssuesInput issues;

	@DataBoundConstructor
	public NewIssuesStep(final IssuesInput issues) {
		this.issues = issues;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraNewIssues";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Create New Issues";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<BasicIssues>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		private transient Run<?, ?> run;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient NewIssuesStep step;

		@Override
		protected ResponseData<BasicIssues> run() throws Exception {

			ResponseData<BasicIssues> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Creating new Issues: " + step.getIssues());
				for(IssueInput issue: step.getIssues().getIssueUpdates()) {
					final String description = issue.getFields().getDescription() + "\n Created by: \nBuild URL: " + buildUrl + "\nBuild User: [~" + buildUser +"]";
					issue.getFields().setDescription(description);
				}
				response = jiraService.createIssues(step.getIssues());
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
