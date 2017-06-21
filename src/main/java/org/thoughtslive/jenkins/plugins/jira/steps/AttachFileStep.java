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
 *          Step to attach a file to an issue.
 */
public class AttachFileStep extends BasicJiraStep {

    @Getter
    private final String idOrKey;

    @Getter
    private final String attachmentPath;

    @DataBoundConstructor
    public AttachFileStep(final String idOrKey, final String attachmentPath) {
        this.idOrKey = idOrKey;
        this.attachmentPath = attachmentPath;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends JiraStepDescriptorImpl {

        @Override
        public String getFunctionName() {
            return "jiraAttachFile";
        }

        @Override
        public String getDisplayName() {
            return getPrefix() + "Attach a file to an issue";
        }
    }

    public static class Execution extends JiraStepExecution<ResponseData<Object>> {

        private static final long serialVersionUID = -4127722455057889625L;

        private final AttachFileStep step;

        protected Execution(final AttachFileStep step, final StepContext context)
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
                final File attachment = new File(step.getAttachmentPath());

                if (idOrKey == null) {
                    errorMessage = "ID or key is null or empty";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }

                if (!attachment.exists()) {
                    errorMessage = "Attachment does not exist";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }
            }
            return response;
        }

        @Override
        protected ResponseData<Object> run() throws Exception {
            ResponseData<Object> responseData = verifyInput();

            if (responseData == null) {
                logger.println("JIRA: Site - " + siteName + " - Attaching file: " + new File(step.getAttachmentPath()).getAbsolutePath());
                responseData = jiraService.attachFile(step.getIdOrKey(), new File(step.getAttachmentPath()));
            }
            return logResponse(responseData);
        }
    }
}
