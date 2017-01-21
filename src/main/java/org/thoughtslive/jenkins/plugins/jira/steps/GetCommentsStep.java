package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comments;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Issue comments.
 *
 * @author Naresh Rayapati
 */
public class GetCommentsStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@DataBoundConstructor
	public GetCommentsStep(final String idOrKey) {
		this.idOrKey = idOrKey;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetComments";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Issue Comments";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Comments>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetCommentsStep step;

		@Override
		protected ResponseData<Comments> run() throws Exception {

			ResponseData<Comments> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying issue: " + step.getIdOrKey() + " comments");
				response = jiraService.getComments(step.getIdOrKey());
			}

			return logResponse(response);
		}
	}
}
