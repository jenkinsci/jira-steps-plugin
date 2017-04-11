
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Fields implements Serializable {

  private static final long serialVersionUID = 9022728696230161151L;

  @JsonProperty("parent")
  private BasicIssue parent;

  @JsonProperty("fixVersions")
  private List<Version> fixVersions = null;

  @JsonProperty("resolution")
  private Resolution resolution;

  @JsonProperty("lastViewed")
  private DateTime lastViewed;

  @JsonProperty("priority")
  private Priority priority;

  @JsonProperty("labels")
  private Set<String> labels = null;

  @JsonProperty("versions")
  private List<Version> versions = null;

  @JsonProperty("issuelinks")
  private List<IssueLink> issuelinks = null;

  @JsonProperty("assignee")
  private User assignee;

  @JsonProperty("status")
  private Status status;

  @JsonProperty("components")
  private List<Component> components = null;

  @JsonProperty("creator")
  private User creator;

  @JsonProperty("subtasks")
  private List<Issue> subtasks = null;

  @JsonProperty("reporter")
  private User reporter;

  @JsonProperty("votes")
  private Votes votes;

  @JsonProperty("worklog")
  private Worklogs worklog;

  @JsonProperty("issuetype")
  private IssueType issuetype;

  @JsonProperty("project")
  private Project project;

  @JsonProperty("resolutiondate")
  private DateTime resolutiondate;

  @JsonProperty("watches")
  private Watches watches;

  @JsonProperty("created")
  private DateTime created;

  @JsonProperty("updated")
  private DateTime updated;

  @JsonProperty("description")
  private String description;

  @JsonProperty("timetracking")
  private TimeTracking timetracking;

  @JsonProperty("attachment")
  private List<Attachment> attachment = null;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("duedate")
  private DateTime duedate;

  @JsonProperty("comment")
  private Comments comment;

}
