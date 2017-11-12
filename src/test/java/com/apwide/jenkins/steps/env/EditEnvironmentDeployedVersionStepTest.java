package com.apwide.jenkins.steps.env;

import static com.apwide.jenkins.api.ResponseData.toResponseData;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hudson.AbortException;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;

import com.apwide.jenkins.api.ResponseData;
import com.apwide.jenkins.steps.BaseStepTest;
import com.apwide.jenkins.steps.env.EditEnvironmentDeployedVersionStep;

@PrepareForTest({ EditEnvironmentDeployedVersionStep.class })
public class EditEnvironmentDeployedVersionStepTest extends BaseStepTest {

    private void assertSuccess(EditEnvironmentDeployedVersionStep step, int serviceCallCount) {
	try {
	    Object returnedObject = step.start(contextMock).run();
	    assertNull(returnedObject);
	    verify(apwideServiceMock, times(serviceCallCount)).updateEnvironmentDeployedVersion(step.getApplicationName(), step.getCategoryName(),
		    step.getVersionName());
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void assertFailure(EditEnvironmentDeployedVersionStep step, int serviceCallCount, String message) {
	assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
	    step.start(contextMock).run();
	}).withMessageContaining(message).withStackTraceContaining("AbortException").withNoCause();

	verify(apwideServiceMock, times(serviceCallCount)).updateEnvironmentDeployedVersion(step.getApplicationName(), step.getCategoryName(),
		step.getVersionName());
    }

    @Test
    public void testParameterChecks() throws Exception {
	EditEnvironmentDeployedVersionStep step;

	step = new EditEnvironmentDeployedVersionStep("", "Staging", "V 1.1");
	assertFailure(step, 0, "applicationName");

	step = new EditEnvironmentDeployedVersionStep(null, "Staging", "V 1.1");
	assertFailure(step, 0, "applicationName");

	step = new EditEnvironmentDeployedVersionStep("eCommerce", "", "V 1.1");
	assertFailure(step, 0, "categoryName");

	step = new EditEnvironmentDeployedVersionStep("eCommerce", null, "V 1.1");
	assertFailure(step, 0, "categoryName");

	setFailOnError(false);
	assertSuccess(step, 0);
    }

    @Test
    public void testServiceCalls() throws Exception {

	ResponseDataBuilder<Void> builder = ResponseData.builder();
	when(apwideServiceMock.updateEnvironmentDeployedVersion(anyString(), anyString(), anyString())).thenReturn(
		toResponseData(builder.successful(true).code(200).message("Success").build()));

	EditEnvironmentDeployedVersionStep step = new EditEnvironmentDeployedVersionStep("eCommerce", "Staging", null);
	assertSuccess(step, 1);

	builder = ResponseData.builder();
	when(apwideServiceMock.updateEnvironmentDeployedVersion(anyString(), anyString(), anyString())).thenReturn(
		toResponseData(builder.successful(false).code(404).error("Environment not found").build()));

	step = new EditEnvironmentDeployedVersionStep("eCommerce", "Staging", null);
	assertFailure(step, 2, "Environment not found");

	setFailOnError(false);
	step = new EditEnvironmentDeployedVersionStep("eCommerce", "Staging", null);
	assertSuccess(step, 3);

    }
}
