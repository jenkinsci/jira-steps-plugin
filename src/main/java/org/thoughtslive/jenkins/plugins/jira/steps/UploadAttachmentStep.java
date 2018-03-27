package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import java.io.IOException;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to upload a file from workspace to an issue.
 *
 * @author Naresh Rayapati
 */
public class UploadAttachmentStep extends BasicJiraStep {

  private static final long serialVersionUID = 2996407840986266627L;

  @Getter
  private final String idOrKey;

  @Getter
  private final String file;

  @DataBoundConstructor
  public UploadAttachmentStep(final String idOrKey, final String file) {
    this.idOrKey = idOrKey;
    this.file = file;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraUploadAttachment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Attach a file from workspace to an issue";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = 7064983919695548462L;

    private final UploadAttachmentStep step;

    protected Execution(final UploadAttachmentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {

        final String idOrKey = Util.fixEmpty(step.getIdOrKey());
        if (idOrKey == null) {
          errorMessage = "ID or key is null or empty";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        FilePath ws = getContext().get(FilePath.class);
        assert ws != null;
        FilePath path;

        if (Util.fixEmpty(step.getFile()) != null) {
          path = ws.child(step.getFile());
        } else {
          errorMessage = "file is null or empty";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }

        if (!path.exists()) {
          errorMessage = path.getRemote() + " does not exist.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }
        if (path.isDirectory()) {
          errorMessage = path.getRemote() + " is a directory.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {
      ResponseData<Object> responseData = verifyInput();

      if (responseData == null) {
        FilePath ws = getContext().get(FilePath.class);
        FilePath path = ws.child(step.getFile());

        logger.println(
            "JIRA: Site - " + siteName + " - Attaching file: " + path.getRemote() + " to " + step
                .getIdOrKey());
        byte[] bytes = IOUtils.toByteArray(path.read());
        responseData = jiraService.uploadAttachment(step.getIdOrKey(), path.getRemote(), bytes);
      }
      return logResponse(responseData);
    }
  }
}