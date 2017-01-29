package org.thoughtslive.jenkins.plugins.jira.steps;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.thoughtslive.jenkins.plugins.jira.api.IssueLinkTypes;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Step to query a JIRA Issue linkss types so that we can link issues later.
 *
 * @author Naresh Rayapati
 */
public class GetIssueLinkTypesStep extends BasicJiraStep {

	private static final long serialVersionUID = 7300279362207875286L;

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraGetIssueLinkTypes";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Get Issue Link Types";
		}

	}

	public static class Execution extends JiraStepExecution<ResponseData<IssueLinkTypes>> {

		private static final long serialVersionUID = -1387617043703686867L;

		@StepContextParameter
		transient Run<?, ?> run;

		@StepContextParameter
		transient TaskListener listener;

		@StepContextParameter
		transient EnvVars envVars;

		@Inject
		transient GetIssueLinkTypesStep step;

		@Override
		protected ResponseData<IssueLinkTypes> run() throws Exception {

			ResponseData<IssueLinkTypes> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Querying All Issue Link Types");
				response = jiraService.getIssueLinkTypes();
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			return verifyCommon(step, listener, envVars, run);
		}
	}
}
