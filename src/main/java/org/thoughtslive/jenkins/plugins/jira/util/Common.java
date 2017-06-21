package org.thoughtslive.jenkins.plugins.jira.util;

import hudson.EnvVars;
import org.thoughtslive.jenkins.plugins.jira.api.Attachments;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Common utility functions.
 * 
 * @author Naresh Rayapati
 *
 */
public class Common {

  /**
   * Empty check for string.
   * 
   * @param str
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
   * @throws IOException
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
   * @param throwable
   * @return {@link Throwable}
   */
  public static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() != null)
      return getRootCause(throwable.getCause());
    return throwable;
  }

  public static String findAttachmentId(final Attachments attachments, final String attachmentName) throws IOException {
    return filterMatchingAttachmentId(attachments, attachmentName);
  }

  public static void saveFile(final byte[] binaryFileContent, final String targetLocation) throws IOException {
    Files.write(Paths.get(new File(targetLocation).getAbsolutePath()), binaryFileContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private static String filterMatchingAttachmentId(final Attachments attachments, final String attachmentName) {
    List<String> foundAttachmentIds = new ArrayList<>();
    attachments.getFields().getAttachment().stream()
            .filter(s -> s.getFilename().equals(attachmentName))
            .forEach(s -> foundAttachmentIds.add(s.getId()));
    validateIds(foundAttachmentIds, attachmentName);
    return foundAttachmentIds.get(0);
  }

  private static void validateIds(final List<String> attachmentIdList, final String attachmentName) {
    if (attachmentIdList.size() == 0) {
      throw new RuntimeException("No attachment with name " + attachmentName + " was found");
    } else if (attachmentIdList.size() > 1) {
      throw new RuntimeException("Multiple attachments with the same name were found: " + attachmentName);
    }
  }
}
