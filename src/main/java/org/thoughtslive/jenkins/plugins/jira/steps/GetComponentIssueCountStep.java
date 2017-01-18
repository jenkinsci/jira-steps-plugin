package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Count;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to query a JIRA Component's issue count.
 *
 * @author Naresh Rayapati
 */
public class GetComponentIssueCountStep extends BasicJiraStep {

	private static final long serialVersionUID = 2327375640378098562L;

	@Getter
	private final int id;

	@DataBoundConstructor
	public GetComponentIssueCountStep(final int id) {
		this.id = id;
	}

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetComponentIssueCount";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Component Issue Count";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<Count>> {

		private static final long serialVersionUID = -821037959812310749L;

		@StepContextParameter
		protected transient TaskListener listener;

		@StepContextParameter
		protected transient EnvVars envVars;

		@Inject
		private transient GetComponentIssueCountStep step;

		@Override
		protected ResponseData<Count> run() throws Exception {

			ResponseData<Count> response = verifyCommon(step, listener, envVars);

			if (response == null) {
				logger.println(
						"JIRA: Site - " + siteName + " - Querying component issue count with id: " + step.getId());
				response = jiraService.getComponentIssueCount(step.getId());
			}

			return logResponse(response);
		}
	}
}
