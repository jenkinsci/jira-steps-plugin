package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = { "successful", "code", "message", "error", "data" })
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> implements Serializable {
	private static final long serialVersionUID = -6177555429105640650L;

	private boolean successful;

	private int code;

	private String message;

	private String error;

	private T data;
}
