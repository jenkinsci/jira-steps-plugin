package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issues implements Serializable {
	private final static long serialVersionUID = 7689184735166991068L;

	@JsonProperty("startAt")
	public int startAt;

	@JsonProperty("maxResults")
	public int maxResults;

	@JsonProperty("total")
	public int total;

	@JsonProperty("issueUpdates")
	public Issue[] issueUpdates;

	@JsonProperty("issues")
	public Issue[] issues;
}
