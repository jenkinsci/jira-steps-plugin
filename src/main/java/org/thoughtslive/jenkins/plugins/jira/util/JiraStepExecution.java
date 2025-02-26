package org.thoughtslive.jenkins.plugins.jira.util;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.empty;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.log;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import hudson.AbortException;
import hudson.EnvVars;
import hudson.Util;
import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserIdCause;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.thoughtslive.jenkins.plugins.jira.Messages;
import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.Site.LoginType;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;
import org.thoughtslive.jenkins.plugins.jira.steps.BasicJiraStep;

/**
 * Common Execution for all JIRA steps.
 *
 * @param <T> the type of the return value (may be {@link Void})
 * @author Naresh Rayapati
 * @see SynchronousNonBlockingStepExecution
 */
public abstract class JiraStepExecution<T> extends SynchronousNonBlockingStepExecution<T> {

  private static final long serialVersionUID = 3856797875872780808L;

  public transient JiraService jiraService = null;
  protected transient PrintStream logger = null;
  protected transient String siteName = null;
  protected transient boolean failOnError = true;
  protected transient String buildUserId = null;
  protected transient String buildUrl = null;
  private transient Run<?, ?> run;
  private transient TaskListener listener;
  private transient EnvVars envVars;

  protected JiraStepExecution(final StepContext context) throws IOException, InterruptedException {
    super(context);
    run = context.get(Run.class);
    listener = context.get(TaskListener.class);
    envVars = context.get(EnvVars.class);
  }

  /**
   * Return the current build user.
   *
   * @param causes build causes.
   * @return user name.
   */
  protected static String prepareBuildUserId(List<Cause> causes) {
    String buildUser = "anonymous";
    if (causes != null && causes.size() > 0) {
      if (causes.get(0) instanceof UserIdCause) {
        buildUser = ((UserIdCause) causes.get(0)).getUserId();
      } else if (causes.get(0) instanceof UpstreamCause) {
        List<Cause> upstreamCauses = ((UpstreamCause) causes.get(0)).getUpstreamCauses();
        buildUser = prepareBuildUserId(upstreamCauses);
      }
    }
    return Util.fixEmpty(buildUser) == null ? "anonymous" : buildUser;
  }

  /**
   * Verifies the common input for all the stesp.
   *
   * @return response if JIRA_SITE is empty or if there is no site configured with JIRA_SITE.
   * @throws AbortException when failOnError is true and JIRA_SITE is missing.
   */
  @SuppressWarnings("hiding")
  protected <T> ResponseData<T> verifyCommon(final BasicJiraStep step) throws AbortException {

    logger = listener.getLogger();

    String errorMessage = null;
    siteName = empty(step.getSite()) ? envVars.get("JIRA_SITE") : step.getSite();
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
      if (jiraService == null) {
        if (LoginType.CREDENTIAL.name().equals(site.getLoginType())) {
          // at build time use of credentials must be checked against the user who run the build, see https://plugins.jenkins.io/authorize-project
          StandardUsernameCredentials usernameCredentials = CredentialsProvider.findCredentialById(
              site.getCredentialsId(), StandardUsernameCredentials.class, run,
              Collections.emptyList());
          StringCredentials tokenCredentials = CredentialsProvider.findCredentialById(
              site.getCredentialsId(), StringCredentials.class, run,
              Collections.emptyList());
          if (usernameCredentials == null && tokenCredentials == null) {
            throw new AbortException(Messages.Site_invalidCredentialsId());
          }
        }
        jiraService = site.getService();
      }
    }

    if (errorMessage != null) {
      return buildErrorResponse(new RuntimeException(errorMessage));
    }

    buildUserId = prepareBuildUserId(run.getCauses());
    buildUrl = envVars.get("BUILD_URL");

    return null;
  }

  /**
   * Log code and error message if any.
   *
   * @return same response back.
   * @throws AbortException if failOnError is true and response is not successful.
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
   * Adds Job info to the given message.
   *
   * @return message added with metadata.
   */
  protected String addPanelMeta(final String message) {
    return message + "\n{panel}Automatically created by: [~" + buildUserId + "] from [Build URL|"
        + buildUrl + "]{panel}";
  }

  /**
   * Adds Job info to the given message.
   *
   * @return message added with metadata.
   */
  protected String addMeta(final String message) {
    return message + "\nAutomatically created by: " + buildUserId + " from " + buildUrl;
  }

  @SuppressWarnings("hiding")
  protected abstract <T> ResponseData<T> verifyInput() throws Exception;
}
