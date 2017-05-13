package org.thoughtslive.jenkins.plugins.jira.api.input;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.IssueType;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.User;
import org.thoughtslive.jenkins.plugins.jira.api.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class FieldsInput implements Serializable {

  private static final long serialVersionUID = 466100668894754241L;

  @JsonProperty("parent")
  private BasicIssue parent;

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
  private Set<String> labels;

  // Only name is required
  @JsonProperty("assignee")
  private User assignee;

  // Only Id is required
  @JsonProperty("components")
  private List<Component> components;

  // Only Id is required
  @JsonProperty("fixVersions")
  private List<Version> fixVersions;

}
