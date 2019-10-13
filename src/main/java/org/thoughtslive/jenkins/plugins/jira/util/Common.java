package org.thoughtslive.jenkins.plugins.jira.util;

import hudson.EnvVars;
import java.io.IOException;
import java.io.PrintStream;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import retrofit2.Response;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.ConsoleHandler;

/**
 * Common utility functions.
 *
 * @author Naresh Rayapati
 */
public class Common {

  /**
   * Empty check for string.
   *
   * @return true if given string is null or empty.
   */
  public static boolean empty(final String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Attaches the "/" at end of given url.
   *
   * @param url url as a string.
   * @return url which ends with "/"
   */
  public static String sanitizeURL(String url) {
    if (!url.endsWith("/")) {
      url = url + "/";
    }
    return url;
  }

  /**
   * Write a message to the given print stream.
   *
   * @param logger {@link PrintStream}
   * @param message to log.
   */
  public static void log(final PrintStream logger, final Object message) {
    if (logger != null) {
      logger.println(message);
    }
  }

  public static String getJobName(final EnvVars envVars) {
    return envVars.get("JOB_NAME");
  }

  /**
   * Returns build number from the given Environemnt Vars.
   *
   * @param logger {@link PrintStream}
   * @param envVars {@link EnvVars}
   * @return build number of current job.
   */
  public static String getBuildNumber(final PrintStream logger, final EnvVars envVars) {
    String answer = envVars.get("BUILD_NUMBER");
    if (answer == null) {
      log(logger, "No BUILD_NUMBER!");
      return "1";
    }
    return answer;
  }

  /**
   * Converts Retrofit's {@link Response} to {@link ResponseData}
   *
   * @param response instance of {@link Response}
   * @return an instance of {@link ResponseData}
   */
  public static <T> ResponseData<T> parseResponse(final Response<T> response) throws IOException {
    final ResponseDataBuilder<T> builder = ResponseData.builder();
    builder.successful(response.isSuccessful()).code(response.code()).message(response.message());
    if (!response.isSuccessful()) {
      final String errorMessage = response.errorBody().string();
      builder.error(errorMessage);
    } else {
      builder.data(response.body());
    }
    return builder.build();
  }

  /**
   * Builds error response from the given exception.
   *
   * @param e instance of {@link Exception}
   * @return an instance of {@link ResponseData}
   */
  public static <T> ResponseData<T> buildErrorResponse(final Exception e) {
    final ResponseDataBuilder<T> builder = ResponseData.builder();
    final String errorMessage = getRootCause(e).getMessage();
    return builder.successful(false).code(-1).error(errorMessage).build();
  }

  /**
   * Returns actual Cause from the given exception.
   *
   * @return {@link Throwable}
   */
  public static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() != null) {
      return getRootCause(throwable.getCause());
    }
    return throwable;
  }

  /**
   * Returns logger that logs to jenkins web console
   *
   * @return an instance of {@link Logger}
   */
  public static Logger getLogger() {
      Logger consoleLogger = LogManager.getLogManager().getLogger("hudson.WebAppMain");
      consoleLogger.addHandler(new ConsoleHandler());
      return consoleLogger;
  }
}
