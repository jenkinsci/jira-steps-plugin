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
 * Step to update JIRA issue comment.
 * 
 * @author Naresh Rayapati
 *
 */
public class UpdateCommentStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final String idOrKey;

	@Getter
	private final Comment comment;

	@DataBoundConstructor
	public UpdateCommentStep(final String idOrKey, final Comment comment) {
		this.idOrKey = idOrKey;
		this.comment = comment;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraUpdateComment";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Update Issue Comment";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Comment>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient UpdateCommentStep step;

		@Override
		protected ResponseData<Comment> run() throws Exception {

			ResponseData<Comment> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Updating comment: "+ step.getComment() +" on issue: " + step.getIdOrKey());
				response = jiraService.updateComment(step.getIdOrKey(), step.getComment());
			}

			return logResponse(response);
		}
	}
}
