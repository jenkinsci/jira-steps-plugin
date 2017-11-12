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
import com.apwide.jenkins.steps.env.EditEnvironmentStatusStep;

@PrepareForTest({ EditEnvironmentStatusStep.class })
public class EditEnvironmentStatusStepTest extends BaseStepTest {

    private void assertSuccess(EditEnvironmentStatusStep step, int serviceCallCount) {
	try {
	    Object returnedObject = step.start(contextMock).run();
	    assertNull(returnedObject);
	    verify(apwideServiceMock, times(serviceCallCount)).updateEnvironmentStatus(step.getApplicationName(), step.getCategoryName(),
		    step.getStatusId(), step.getStatusName());
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void assertFailure(EditEnvironmentStatusStep step, int serviceCallCount, String message) {
	assertThatExceptionOfType(AbortException.class).isThrownBy(() -> {
	    step.start(contextMock).run();
	}).withMessageContaining(message).withStackTraceContaining("AbortException").withNoCause();

	verify(apwideServiceMock, times(serviceCallCount)).updateEnvironmentStatus(step.getApplicationName(), step.getCategoryName(),
		step.getStatusId(), step.getStatusName());
    }

    @Test
    public void testParameterChecks() throws Exception {
	EditEnvironmentStatusStep step;

	step = new EditEnvironmentStatusStep("", "Staging", "test comment", null);
	assertFailure(step, 0, "applicationName");

	step = new EditEnvironmentStatusStep(null, "Staging", "test comment", null);
	assertFailure(step, 0, "applicationName");

	step = new EditEnvironmentStatusStep("eCommerce", "", "test comment", null);
	assertFailure(step, 0, "categoryName");

	step = new EditEnvironmentStatusStep("eCommerce", null, "test comment", null);
	assertFailure(step, 0, "categoryName");

	setFailOnError(false);
	assertSuccess(step, 0);
    }

    @Test
    public void testServiceCalls() throws Exception {

	ResponseDataBuilder<Void> builder = ResponseData.builder();
	when(apwideServiceMock.updateEnvironmentStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(
		toResponseData(builder.successful(true).code(200).message("Success").build()));

	EditEnvironmentStatusStep step = new EditEnvironmentStatusStep("eCommerce", "Staging", "", null);
	assertSuccess(step, 1);

	builder = ResponseData.builder();
	when(apwideServiceMock.updateEnvironmentStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(
		toResponseData(builder.successful(false).code(404).error("Environment not found").build()));

	step = new EditEnvironmentStatusStep("eCommerce", "Staging", "", null);
	assertFailure(step, 2, "Environment not found");

	setFailOnError(false);
	step = new EditEnvironmentStatusStep("eCommerce", "Staging", "", null);
	assertSuccess(step, 3);

    }
}
