package com.apwide.jenkins.api;

import java.io.IOException;

import org.thoughtslive.jenkins.plugins.jira.api.ResponseData.ResponseDataBuilder;
import org.thoughtslive.jenkins.plugins.jira.util.Common;

import retrofit2.Response;

public class ResponseHandler {

    private static boolean isSuccessfulStatus(int status) {
	return (status >= 200 && status <= 399);
    }

    public static <T> ResponseData<T> buildErrorResponse(final Exception e) {
	return ResponseData.toResponseData(Common.buildErrorResponse(e));
    }

    public static <T> ResponseData<T> parseResponse(final Response<T> response) throws IOException {
	final ResponseDataBuilder<T> builder = ResponseData.builder();
	builder.successful(isSuccessfulStatus(response.code())).code(response.code()).message(response.message());
	if (!response.isSuccessful()) {
	    final String errorMessage = response.errorBody().string();
	    builder.error(errorMessage);
	} else {
	    builder.data(response.body());
	}
	return ResponseData.toResponseData(builder.build());
    }

}
