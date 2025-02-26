package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import java.io.IOException;
import java.io.OutputStream;
import lombok.Getter;
import okhttp3.ResponseBody;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.InputBuilder;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to download a file to workspace from an issue.
 *
 * @author Naresh Rayapati
 */
public class DownloadAttachmentStep extends BasicJiraStep {

  private static final long serialVersionUID = 6317067114642701582L;

  @Getter
  public final String id;

  @Getter
  public final String file;

  @Getter
  public boolean override;

  @DataBoundConstructor
  public DownloadAttachmentStep(final String id, final String file,
      final boolean override) {
    this.id = id;
    this.file = file;
    this.override = override;
  }

  @Override
  public StepExecution start(final StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraDownloadAttachment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Download a file to workspace (directory is optional)";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    private static final long serialVersionUID = -1459989930759928081L;

    private final DownloadAttachmentStep step;

    protected Execution(final DownloadAttachmentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      final String attachmentId = Util.fixEmpty(step.getId());

      if (response == null) {
        if (attachmentId == null) {
          errorMessage = "id is null or empty";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }
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

      if (path.isDirectory()) {
        errorMessage = path.getRemote() + " is a directory.";
        return buildErrorResponse(new RuntimeException(errorMessage));
      }

      if (path.exists()) {
        if (!step.override) {
          errorMessage = path.getRemote() + " already exist.";
          return buildErrorResponse(new RuntimeException(errorMessage));
        }
      }

      return response;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {
      ResponseData<Object> responseData = verifyInput();

      if (responseData == null) {
        logger.println("JIRA: Site - " + siteName + " - Downloading attachment with Id: " + step
            .getId());
        responseData = jiraService.getAttachment(step.getId());
        if (responseData.isSuccessful()) {
          final String attachmentLink = InputBuilder.getAttachmentLink(responseData.getData());
          ResponseData<ResponseBody> response = jiraService.downloadAttachment(attachmentLink);
          if (response.isSuccessful()) {
            FilePath ws = getContext().get(FilePath.class);
            FilePath path = ws.child(step.getFile());
            logger.println(
                "JIRA: Site - " + siteName + " - Downloading " + attachmentLink + " file to: "
                    + path.getRemote() + ", overriding an existing file?: " + step.override);
            try (OutputStream os = path.write()) {
              os.write(response.getData().bytes());
            } catch (Exception e) {
              responseData = buildErrorResponse(e);
            }
            responseData.setData(null);
          } else {
            final ResponseData.ResponseDataBuilder builder = ResponseData.builder();
            builder.successful(response.isSuccessful()).code(response.getCode())
                .message(response.getMessage()).error(response.getError());
            return builder.build();
          }
        }
      }
      return logResponse(responseData);
    }
  }
}