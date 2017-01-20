
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.kohsuke.stapler.DataBoundConstructor;

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
public class Version implements Serializable {

	private final static long serialVersionUID = 2195734358892274719L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("archived")
	private Boolean archived;

	@JsonProperty("released")
	private Boolean released;

	@JsonProperty("startDate")
	private DateTime startDate;

	@JsonProperty("releaseDate")
	private DateTime releaseDate;

	@JsonProperty("userStartDate")
	private DateTime userStartDate;

	@JsonProperty("userReleaseDate")
	private DateTime userReleaseDate;

	@JsonProperty("project")
	private String project;

	@JsonProperty("projectId")
	private int projectId;

}