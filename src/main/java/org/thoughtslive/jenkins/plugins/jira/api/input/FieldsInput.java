package org.thoughtslive.jenkins.plugins.jira.api.input;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.IssueType;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.User;
import org.thoughtslive.jenkins.plugins.jira.api.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@SuppressFBWarnings
public class FieldsInput implements Serializable {

	private static final long serialVersionUID = 7563845457157186901L;

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("description")
	private String description;

	// Only Id is required
	@JsonProperty("issuetype")
	private IssueType issuetype;

	// Only Id is required
	@JsonProperty("project")
	private Project project;

	// The following are optional.
	@JsonProperty("labels")
	private String[] labels;

	// Only name is required
	@JsonProperty("assignee")
	private User assignee;

	// Only Id is required
	@JsonProperty("components")
	private Component[] components;

	// Only Id is required
	@JsonProperty("fixVersions")
	private Version[] fixVersions;

}