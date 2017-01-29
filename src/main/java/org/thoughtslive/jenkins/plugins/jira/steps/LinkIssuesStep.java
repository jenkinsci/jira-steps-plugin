package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import lombok.Getter;

/**
 * Step to Link two issues.
 * 
 * @author Naresh Rayapati
 *
 */
public class LinkIssuesStep extends BasicJiraStep {

	private static final long serialVersionUID = -1881920733234295481L;

	@Getter
	private final String type;

	@Getter
	private final String inwardKey;

	@Getter
	private final String outwardKey;

	@DataBoundConstructor
	public LinkIssuesStep(final String type, final String inwardKey, final String outwardKey) {
		this.type = type;
		this.inwardKey = inwardKey;
		this.outwardKey = outwardKey;
	}

	// Comment is optional.
	@Getter
	@DataBoundSetter
	private String comment;

	@Extension
	public static class DescriptorImpl extends JiraStepDescriptorImpl {

		public DescriptorImpl() {
			super(Execution.class);
		}

		@Override
		public String getFunctionName() {
			return "jiraLinkIssues";
		}

		@Override
		public String getDisplayName() {
			return getPrefix() + "Link Issues";
		}

		@Override
		public boolean isMetaStep() {
			return true;
		}
	}

	public static class Execution extends JiraStepExecution<ResponseData<Void>> {

		private static final long serialVersionUID = -1666683149182699538L;

		@StepContextParameter
		transient Run<?, ?> run;

		@StepContextParameter
		transient TaskListener listener;

		@StepContextParameter
		transient EnvVars envVars;

		@Inject
		transient LinkIssuesStep step;

		@Override
		protected ResponseData<Void> run() throws Exception {

			ResponseData<Void> response = verifyInput();

			if (response == null) {
				logger.println("JIRA: Site - " + siteName + " - Linking issue(inward): " + step.getInwardKey() + " and issue(outward)" + step.getOutwardKey() + " with type: "
						+ step.getType());
				response = jiraService.linkIssues(step.getType(), step.getInwardKey(), step.getOutwardKey(), step.getComment());
			}

			return logResponse(response);
		}

		@Override
		protected <T> ResponseData<T> verifyInput() throws Exception {
			String errorMessage = null;
			ResponseData<T> response = verifyCommon(step, listener, envVars, run);

			if (response == null) {
				final String type = Util.fixEmpty(step.getType());
				final String inwardKey = Util.fixEmpty(step.getInwardKey());
				final String outwardKey = Util.fixEmpty(step.getOutwardKey());

				if (type == null) {
					errorMessage = "type is empty or null.";
				}

				if (inwardKey == null) {
					errorMessage = "inwardKey is empty or null.";
				}

				if (outwardKey == null) {
					errorMessage = "outwardKey is empty or null.";
				}

				if (errorMessage != null) {
					response = buildErrorResponse(new RuntimeException(errorMessage));
				}
			}
			return response;
		}
	}
}
