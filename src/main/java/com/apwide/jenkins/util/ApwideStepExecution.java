package com.apwide.jenkins.util;

import static com.apwide.jenkins.api.ResponseHandler.buildErrorResponse;
import static com.apwide.jenkins.util.ApwideCommon.log;
import hudson.AbortException;
import hudson.EnvVars;
import hudson.model.TaskListener;
import hudson.model.Run;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import com.apwide.jenkins.api.ResponseData;
import com.apwide.jenkins.service.ApwideService;

public abstract class ApwideStepExecution<T, S extends ApwideJiraStep> extends JiraStepExecution<T> {

    private static final long serialVersionUID = -4495525306914574228L;

    protected transient ApwideService apwideService = null;

    private transient Run<?, ?> run;
    private transient TaskListener listener;
    private transient EnvVars envVars;

    protected final S step;

    protected ApwideStepExecution(StepContext context, S step) throws IOException, InterruptedException {
	super(context);
	run = context.get(Run.class);
	listener = context.get(TaskListener.class);
	envVars = context.get(EnvVars.class);
	logger = listener.getLogger();
	this.step = step;
    }

    protected ResponseData<T> runtimeException(String message) {
	return buildErrorResponse(new RuntimeException(message));
    }

    protected ResponseData<T> verifyCommon(final S step) throws AbortException {
	org.thoughtslive.jenkins.plugins.jira.api.ResponseData<T> responseDataOrig = super.verifyCommon(step);
	ResponseData<T> response = ResponseData.toResponseData(responseDataOrig);

	if (response != null && !response.isSuccessful()) {
	    return response;
	}

	final ApwideSite site = ApwideSite.get(siteName);

	if (site == null) {
	    return raiseError(runtimeException("No APWIDE JIRA site configured with " + siteName + " name."));
	} else {

	    if (apwideService == null)
		apwideService = site.getApwideService();

	    if (apwideService == null)
		return runtimeException("Problem during initialization of APWIDE service. Site name:" + siteName);

	    buildUserId = prepareBuildUser(run.getCauses());
	    buildUrl = envVars.get("BUILD_URL");

	    return success();
	}
    }

    @Override
    protected ResponseData<T> verifyInput() throws Exception {

	ResponseData<T> response = verifyCommon(step);

	if (!response.isSuccessful()) {
	    return raiseError(response);
	}

	try {

	    response = checkParams();

	    if (!response.isSuccessful())
		return raiseError(response);
	    else
		return response;

	} catch (Exception e) {
	    return raiseError(buildErrorResponse(e));
	}

    }

    protected abstract ResponseData<T> execute();

    @Override
    public T run() throws Exception {

	ResponseData<T> response = verifyInput();

	if (response.isSuccessful()) {
	    log(logger, "APWIDE: Jira Site - " + siteName + " - Updating environment status: " + step);
	    try {
		response = execute();
	    } catch (Exception e) {
		response = buildErrorResponse(e);
		raiseError(response);
	    }
	}

	logResponse(response);

	if (!response.isSuccessful()) {
	    raiseError(response);
	    return response.getData();
	}

	return response.getData();
    }

    private ResponseData<T> raiseError(ResponseData<T> response) throws AbortException {
	if (failOnError)
	    throw new AbortException(response.getError());
	else
	    return response;
    }

    protected abstract ResponseData<T> checkParams() throws Exception;

    private void logResponse(ResponseData<T> response) throws AbortException {
	if (response.isSuccessful()) {
	    log(logger, "Successful. Code: " + response.getCode());
	} else {
	    log(logger, "Error Code: " + response.getCode());
	    log(logger, "Error Message: " + response.getError());
	}
    }

    protected ResponseData<T> success(T data) {
	return new ResponseData<T>(true, 200, "Success", null, data);
    }

    protected ResponseData<T> success() {
	return new ResponseData<T>(true, 200, "Success", null, null);
    }

}
