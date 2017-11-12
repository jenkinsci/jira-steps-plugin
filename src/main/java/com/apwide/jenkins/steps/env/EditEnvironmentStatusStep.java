package com.apwide.jenkins.steps.env;

import static com.apwide.jenkins.util.ApwideStepChecker.checkNotNull;
import hudson.Extension;

import java.io.IOException;

import lombok.Getter;
import lombok.ToString;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import com.apwide.jenkins.api.ResponseData;
import com.apwide.jenkins.util.ApwideJiraStep;
import com.apwide.jenkins.util.ApwideStepDescriptorImpl;
import com.apwide.jenkins.util.ApwideStepExecution;

@ToString(of = { "applicationName", "categoryName", "statusName", "statusId" })
public class EditEnvironmentStatusStep extends ApwideJiraStep {

    private static final long serialVersionUID = -5047755533376456765L;

    @Getter
    private final String applicationName;

    @Getter
    private final String categoryName;

    @Getter
    private final String statusName;

    @Getter
    private final String statusId;

    @DataBoundConstructor
    public EditEnvironmentStatusStep(final String applicationName, final String categoryName, final String statusId, final String statusName) {
	this.applicationName = applicationName;
	this.categoryName = categoryName;
	this.statusName = statusName;
	this.statusId = statusId;
    }

    @Extension
    public static class DescriptorImpl extends ApwideStepDescriptorImpl {

	@Override
	public String getFunctionName() {
	    return "apwideUpdateEnvironmentStatus";
	}

	@Override
	public String getDisplayName() {
	    return getPrefix() + "Update Environment Status";
	}

	@Override
	public boolean isMetaStep() {
	    return true;
	}
    }

    public static class Execution extends ApwideStepExecution<Void, EditEnvironmentStatusStep> {

	private static final long serialVersionUID = -4127725325057889625L;

	protected Execution(final EditEnvironmentStatusStep step, final StepContext context) throws IOException, InterruptedException {
	    super(context, step);
	}

	@Override
	protected ResponseData<Void> checkParams() throws Exception {
	    checkNotNull(step.getApplicationName(), "applicationName");
	    checkNotNull(step.getCategoryName(), "categoryName");
	    return success();
	}

	@Override
	protected ResponseData<Void> execute() {
	    return apwideService.updateEnvironmentStatus(step.getApplicationName(), step.getCategoryName(), step.getStatusId(), step.getStatusName());
	}

    }

    @Override
    public Execution start(StepContext context) throws Exception {
	return new Execution(this, context);
    }

}
