package org.thoughtslive.jenkins.plugins.jira.api;

import org.joda.time.DateTime;
import org.kohsuke.stapler.DataBoundConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.net.URI;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Worklog implements Serializable {

	private static final long serialVersionUID = 8173486033156584525L;

	@JsonProperty("issueUri")
	private URI issueUri;

	@JsonProperty("author")
	private User author;
	
	@JsonProperty("updateAuthor")
	private User updateAuthor;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("creationDate")
	private DateTime creationDate;

	@JsonProperty("updateDate")
	private DateTime updateDate;
	
	@JsonProperty("startDate")
	private DateTime startDate;

	@JsonProperty("minutesSpent")
	private int minutesSpent;

}
