package org.thoughtslive.jenkins.plugins.jira.util;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.empty;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.log;

import java.io.PrintStream;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;
import org.thoughtslive.jenkins.plugins.jira.steps.BasicJiraStep;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.Util;
import hudson.model.TaskListener;

/**
 * Common Execution for all JIRA steps.
 * 
 * @see AbstractSynchronousNonBlockingStepExecution
 * @author Naresh Rayapati
 *
 * @param <T>
 *            the type of the return value (may be {@link Void})
 */
public abstract class JiraStepExecution<T> extends AbstractSynchronousNonBlockingStepExecution<T> {

	private static final long serialVersionUID = -8253380624161445367L;

	protected transient PrintStream logger = null;
	protected transient String siteName = null;
	protected transient JiraService jiraService = null;
	protected transient boolean failOnError = false;

	/**
	 * Verifies the common input for all the stesp.
	 * 
	 * @param step
	 * @param listener
	 *            taskListener
	 * @param envVars
	 *            environment vars.
	 * @return response if JIRA_SITE is empty or if there is no site configured with JIRA_SITE.
	 * @throws AbortException
	 *             when failOnError is true and JIRA_SITE is missing.
	 */
	@SuppressWarnings("hiding")
	protected <T> ResponseData<T> verifyCommon(final BasicJiraStep step, final TaskListener listener, final EnvVars envVars) throws AbortException {

		logger = listener.getLogger();
		String errorMessage = null;
		siteName = empty(step.getSiteName()) ? envVars.get("JIRA_SITE") : step.getSiteName();
		final Site site = Site.get(siteName);
		final String failOnErrorStr = Util.fixEmpty(envVars.get("JIRA_FAIL_ON_ERROR"));

		if (failOnErrorStr == null) {
			failOnError = step.isFailOnError();
		} else {
			failOnError = Boolean.parseBoolean(failOnErrorStr);
		}

		if (empty(siteName)) {
			errorMessage = "JIRA_SITE is empty or null.";
		}

		if (site == null) {
			errorMessage = "No JIRA site configured with " + siteName + " name.";
		} else {
			jiraService = site.getService();
		}

		if(errorMessage != null) {
			return buildErrorResponse(new RuntimeException(errorMessage));
		}
		
		return null;
	}

	/**
	 * Log code and error message if any.
	 * 
	 * @param response
	 * @return same response back.
	 * @throws AbortException
	 *             if failOnError is true and response is not successful.
	 */
	@SuppressWarnings("hiding")
	protected <T> ResponseData<T> logResponse(ResponseData<T> response) throws AbortException {

		if (response.isSuccessful()) {
			log(logger, "Successful. Code: " + response.getCode());
		} else {
			log(logger, "Error Code: " + response.getCode());
			log(logger, "Error Message: " + response.getError());

			if (failOnError) {
				throw new AbortException(response.getError());
			}
		}

		return response;
	}
}
