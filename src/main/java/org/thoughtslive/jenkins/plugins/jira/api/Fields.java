
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
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
public class Fields implements Serializable {

	private final static long serialVersionUID = -423297322860356733L;

	@JsonProperty("fixVersions")
	public List<Version> fixVersions = null;

	@JsonProperty("resolution")
	public Resolution resolution;

	@JsonProperty("lastViewed")
	public DateTime lastViewed;

	@JsonProperty("priority")
	public Priority priority;

	@JsonProperty("labels")
	public Set<String> labels = null;

	@JsonProperty("versions")
	public List<Version> versions = null;

	@JsonProperty("issuelinks")
	public List<IssueLink> issuelinks = null;

	@JsonProperty("assignee")
	public User assignee;

	@JsonProperty("status")
	public Status status;

	@JsonProperty("components")
	public List<Component> components = null;

	@JsonProperty("creator")
	public User creator;

	@JsonProperty("subtasks")
	public List<Issue> subtasks = null;

	@JsonProperty("reporter")
	public User reporter;

	@JsonProperty("votes")
	public Votes votes;

	@JsonProperty("worklog")
	public Worklogs worklog;

	@JsonProperty("issuetype")
	public IssueType issuetype;

	@JsonProperty("project")
	public Project project;

	@JsonProperty("resolutiondate")
	public DateTime resolutiondate;

	@JsonProperty("watches")
	public Watches watches;

	@JsonProperty("created")
	public DateTime created;

	@JsonProperty("updated")
	public DateTime updated;

	@JsonProperty("description")
	public String description;

	@JsonProperty("timetracking")
	public TimeTracking timetracking;

	@JsonProperty("attachment")
	public List<Attachment> attachment = null;

	@JsonProperty("summary")
	public String summary;

	@JsonProperty("duedate")
	public DateTime duedate;

	@JsonProperty("comment")
	public Comments comment;

}
