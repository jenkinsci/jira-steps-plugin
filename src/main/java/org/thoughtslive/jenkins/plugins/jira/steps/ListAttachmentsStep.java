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

import java.io.IOException;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Step to list all information about the attachments of an issue.
 */
public class ListAttachmentsStep extends BasicJiraStep {

    @Getter
    private final String idOrKey;

    @DataBoundConstructor
    public ListAttachmentsStep(final String idOrKey) {
        this.idOrKey = idOrKey;
    }

    @Override
    public StepExecution start(StepContext stepContext) throws Exception {
        return null;
    }

    @Extension
    public static class DescriptorImpl extends JiraStepDescriptorImpl {

        @Override
        public String getFunctionName() {
            return "jiraListAttachments";
        }

        @Override
        public String getDisplayName() {
            return getPrefix() + "Get a list of all attachments attached to an issue";
        }
    }

    public static class Execution extends JiraStepExecution<ResponseData<Object>> {

        private static final long serialVersionUID = -4177432455057889625L;
        private final ListAttachmentsStep step;

        protected Execution(final ListAttachmentsStep step, final StepContext context) throws IOException, InterruptedException {
            super(context);
            this.step = step;
        }

        @Override
        protected <T> ResponseData<T> verifyInput() throws Exception {
            String errorMessage;
            ResponseData<T> response = verifyCommon(step);

            if (response == null) {
                final String idOrKey = Util.fixEmpty(step.getIdOrKey());

                if (idOrKey == null) {
                    errorMessage = "ID or key is null or empty";
                    return buildErrorResponse(new RuntimeException(errorMessage));
                }

            }
            return response;
        }

        @Override
        protected ResponseData<Object> run() throws Exception {
            ResponseData<Object> responseData = verifyInput();

            if (responseData == null) {
                logger.println("JIRA: Site - " + siteName + " - Listing attachments for: " + step.getIdOrKey());
                responseData = jiraService.listAttachments(step.getIdOrKey());
            }
            return logResponse(responseData);
        }
    }
}
