package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import hudson.Util;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import java.io.File;
import java.io.IOException;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Step to download a file from an issue.
 */
public class DownloadAttachmentStep extends BasicJiraStep {

    @Getter
    private final String attachmentName;

    @Getter
    private final String idOrKey;

    @Getter
    private final String targetLocation;

    @DataBoundConstructor
    public DownloadAttachmentStep(final String attachmentName, final String idOrKey, final String targetLocation) {
        this.attachmentName = attachmentName;
        this.idOrKey = idOrKey;
        this.targetLocation = targetLocation;
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
            return getPrefix() + "Download a file which is attached to an issue ";
        }
    }

    public static class Execution extends JiraStepExecution<ResponseData<Object>> {

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

            final String idOrKey = Util.fixEmpty(step.getIdOrKey());
            final String attachmentName = step.getAttachmentName();
            String targetLocation = step.getTargetLocation();

            if (response == null) {
                if (idOrKey == null || idOrKey.isEmpty()) {
                    errorMessage = "ID or key is null or empty";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }

                if (attachmentName == null || attachmentName.isEmpty()) {
                    errorMessage = "Attachment-Name is null or emtpy";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }

                if (targetLocation == null || targetLocation.isEmpty()) {
                    errorMessage = "Target location is null or emtpy";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }
            }
            return response;
        }

        @Override
        protected ResponseData<Object> run() throws Exception {
            ResponseData<Object> responseData = verifyInput();

            if (responseData == null) {
                logger.println("JIRA: Site - " + siteName + " - Downloading file: " + new File(step.getAttachmentName())
                        + "from " + step.getIdOrKey() + "to " + step.getTargetLocation());
                responseData = jiraService.downloadAttachment(step.getIdOrKey(), step.getAttachmentName(), step.getTargetLocation());
            }
            return logResponse(responseData);
        }
    }
}
