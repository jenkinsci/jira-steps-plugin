package org.thoughtslive.jenkins.plugins.jira.util;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.empty;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.log;

import java.io.PrintStream;
import java.util.List;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;
import org.thoughtslive.jenkins.plugins.jira.steps.BasicJiraStep;

import com.google.common.annotations.VisibleForTesting;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.Util;
import hudson.model.Cause;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserIdCause;

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

	private static final long serialVersionUID = 3856797875872780808L;

	protected transient PrintStream logger = null;
	protected transient String siteName = null;
	protected transient JiraService jiraService = null;
	protected transient boolean failOnError = true;
	protected transient String buildUser = null;
	protected transient String buildUrl = null;

	/**
	 * Verifies the common input for all the stesp.
	 * 
	 * @param step
	 * @param listener
	 *            taskListener
	 * @param envVars
	 *            environment vars.
	 * @return response if JIRA_SITE is empty or if there is no site configured
	 *         with JIRA_SITE.
	 * @throws AbortException
	 *             when failOnError is true and JIRA_SITE is missing.
	 */
	@SuppressWarnings("hiding")
	protected <T> ResponseData<T> verifyCommon(final BasicJiraStep step, final TaskListener listener,
			final EnvVars envVars, final Run<?, ?> run) throws AbortException {

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
			jiraService = getJiraService(site);
		}

		if (errorMessage != null) {
			return buildErrorResponse(new RuntimeException(errorMessage));
		}

		buildUser = prepareBuildUser(run.getCauses());
		buildUrl = envVars.get("BUILD_URL");

		return null;
	}

	@VisibleForTesting
	public JiraService getJiraService(final Site site) {
		return site.getService();
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

	/**
	 * Return the current build user.
	 * 
	 * @param causes
	 *            build causes.
	 * @return user name.
	 */
	protected static String prepareBuildUser(List<Cause> causes) {
		String buildUser = "anonymous";
		if (causes != null && causes.size() > 0) {
			if (causes.get(0) instanceof UserIdCause) {
				buildUser = ((UserIdCause) causes.get(0)).getUserName();
			} else if (causes.get(0) instanceof UpstreamCause) {
				List<Cause> upstreamCauses = ((UpstreamCause) causes.get(0)).getUpstreamCauses();
				prepareBuildUser(upstreamCauses);
			}
		}
		return buildUser;
	}

	/**
	 * Adds Job info to the given message.
	 * 
	 * @param message
	 * @return message added with metadata.
	 */
	protected String addPanelMeta(final String message) {
		return message + "\n{panel}Automatically created by: [~" + buildUser + "] from [Build URL|" + buildUrl
				+ "]{panel}";
	}

	/**
	 * Adds Job info to the given message.
	 * 
	 * @param message
	 * @return message added with metadata.
	 */
	protected String addMeta(final String message) {
		return message + "\nAutomatically created by: " + buildUser + " from " + buildUrl;
	}

	@SuppressWarnings("hiding")
	protected abstract <T> ResponseData<T> verifyInput() throws Exception;
}
