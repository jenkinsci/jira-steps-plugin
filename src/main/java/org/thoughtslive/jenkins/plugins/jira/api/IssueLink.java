
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
public class IssueLink implements Serializable {
	private final static long serialVersionUID = 2457522846452464445L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("self")
	private String self;

	@JsonProperty("type")
	private IssueLinkType type;

	@JsonProperty("inwardIssue")
	private Issue inwardIssue;

	@JsonProperty("outwordIssue")
	private Issue outwordIssue;

	@JsonProperty("comment")
	private Comment comment;
}
