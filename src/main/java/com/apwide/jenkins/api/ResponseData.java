package com.apwide.jenkins.api;

public class ResponseData<T> extends org.thoughtslive.jenkins.plugins.jira.api.ResponseData<T> {

    private static final long serialVersionUID = 8602443268648392541L;

    public ResponseData(boolean successful, int code, String message, String error, T data) {
	super(successful, code, message, error, data);
    }

    public static <T> ResponseData<T> toResponseData(org.thoughtslive.jenkins.plugins.jira.api.ResponseData<T> responseData) {
	if (responseData == null)
	    return null;
	else
	    return new ResponseData<T>(responseData.isSuccessful(), responseData.getCode(), responseData.getMessage(), responseData.getError(),
		    responseData.getData());
    }

}
