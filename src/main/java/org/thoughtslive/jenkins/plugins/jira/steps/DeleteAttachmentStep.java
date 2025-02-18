package org.thoughtslive.jenkins.plugins.jira.steps;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

import hudson.Extension;
import hudson.Util;
import java.io.IOException;
import java.io.Serial;

import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to delete JIRA attachment.
 *
 * @author Naresh Rayapati
 */
@Getter
public class DeleteAttachmentStep extends BasicJiraStep {

  @Serial
  private static final long serialVersionUID = -4661648934764886451L;

  private final String id;

  @DataBoundConstructor
  public DeleteAttachmentStep(final String id) {
    this.id = id;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraDeleteAttachment";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Delete Attachment";
    }

  }

  public static class Execution extends JiraStepExecution<ResponseData<Object>> {

    @Serial
    private static final long serialVersionUID = -742172771459279821L;

    private final DeleteAttachmentStep step;

    protected Execution(final DeleteAttachmentStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Object> run() throws Exception {

      ResponseData<Object> response = verifyInput();

      if (response == null) {
        logger.println(
            "JIRA: Site - " + siteName + " - Deleting attachment with Id: " + step.getId()
                + " from an Issue");
        response = jiraService.deleteAttachment(step.getId());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String id = Util.fixEmpty(step.getId());

        if (id == null) {
          errorMessage = "id is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}