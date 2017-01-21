package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Issue comment.
 *
 * @author Naresh Rayapati
 */
public class GetCommentStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final String commentId;

	@DataBoundConstructor
	public GetCommentStep(final String commentId, final String idOrKey) {
		this.commentId = commentId;
		this.idOrKey = idOrKey;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetComment";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Issue Comment";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Comment>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetCommentStep step;

		@Override
		protected ResponseData<Comment> run() throws Exception {

			ResponseData<Comment> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying issue: "+ step.getIdOrKey() +" comment with id: " + step.getCommentId());
				response = jiraService.getComment(step.getIdOrKey(), step.getCommentId());
			}

			return logResponse(response);
		}
	}
}
