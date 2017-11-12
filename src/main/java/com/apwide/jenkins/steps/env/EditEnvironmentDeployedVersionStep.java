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

@ToString(of = { "applicationName", "categoryName", "versionName" })
public class EditEnvironmentDeployedVersionStep extends ApwideJiraStep {

    private static final long serialVersionUID = -5047755533376456765L;

    @Getter
    private final String applicationName;

    @Getter
    private final String categoryName;

    @Getter
    private final String versionName;


    @DataBoundConstructor
    public EditEnvironmentDeployedVersionStep(final String applicationName, final String categoryName, final String versionName) {
	this.applicationName = applicationName;
	this.categoryName = categoryName;
	this.versionName = versionName;
    }

    @Extension
    public static class DescriptorImpl extends ApwideStepDescriptorImpl {

	@Override
	public String getFunctionName() {
	    return "apwideUpdateEnvironmentDeployedVersion";
	}

	@Override
	public String getDisplayName() {
	    return getPrefix() + "Update Environment Deployed Version";
	}

	@Override
	public boolean isMetaStep() {
	    return true;
	}
    }

    public static class Execution extends ApwideStepExecution<Void, EditEnvironmentDeployedVersionStep> {

	private static final long serialVersionUID = -4127725325057889625L;

	protected Execution(final EditEnvironmentDeployedVersionStep step, final StepContext context) throws IOException, InterruptedException {
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
	    return apwideService.updateEnvironmentDeployedVersion(step.getApplicationName(), step.getCategoryName(), step.getVersionName());
	}

    }

    @Override
    public Execution start(StepContext context) throws Exception {
	return new Execution(this, context);
    }

}
