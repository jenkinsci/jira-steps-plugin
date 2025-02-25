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
import org.kohsuke.stapler.DataBoundSetter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

/**
 * Step to Link two issues.
 *
 * @author Naresh Rayapati
 */
@Getter
public class LinkIssuesStep extends BasicJiraStep {

  @Serial
  private static final long serialVersionUID = -1881920733234295481L;

  private final String type;

  private final String inwardKey;

  private final String outwardKey;
  // Comment is optional.
  @DataBoundSetter
  private String comment;

  @DataBoundConstructor
  public LinkIssuesStep(final String type, final String inwardKey, final String outwardKey) {
    this.type = type;
    this.inwardKey = inwardKey;
    this.outwardKey = outwardKey;
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    return new Execution(this, context);
  }

  @Extension
  public static class DescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    public String getFunctionName() {
      return "jiraLinkIssues";
    }

    @Override
    public String getDisplayName() {
      return getPrefix() + "Link Issues";
    }
  }

  public static class Execution extends JiraStepExecution<ResponseData<Void>> {

    @Serial
    private static final long serialVersionUID = -1666683149182699538L;

    private final LinkIssuesStep step;

    protected Execution(final LinkIssuesStep step, final StepContext context)
        throws IOException, InterruptedException {
      super(context);
      this.step = step;
    }

    @Override
    protected ResponseData<Void> run() throws Exception {

      ResponseData<Void> response = verifyInput();

      if (response == null) {
        logger
            .println("JIRA: Site - " + siteName + " - Linking issue(inward): " + step.getInwardKey()
                + " and issue(outward)" + step.getOutwardKey() + " with type: " + step.getType());
        response = jiraService.linkIssues(step.getType(), step.getInwardKey(), step.getOutwardKey(),
            step.getComment());
      }

      return logResponse(response);
    }

    @Override
    protected <T> ResponseData<T> verifyInput() throws Exception {
      String errorMessage = null;
      ResponseData<T> response = verifyCommon(step);

      if (response == null) {
        final String type = Util.fixEmpty(step.getType());
        final String inwardKey = Util.fixEmpty(step.getInwardKey());
        final String outwardKey = Util.fixEmpty(step.getOutwardKey());

        if (type == null) {
          errorMessage = "type is empty or null.";
        }

        if (inwardKey == null) {
          errorMessage = "inwardKey is empty or null.";
        }

        if (outwardKey == null) {
          errorMessage = "outwardKey is empty or null.";
        }

        if (errorMessage != null) {
          response = buildErrorResponse(new RuntimeException(errorMessage));
        }
      }
      return response;
    }
  }
}
