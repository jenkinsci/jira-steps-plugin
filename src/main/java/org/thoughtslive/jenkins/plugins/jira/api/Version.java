
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

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

	private static final long serialVersionUID = 6265539232947567567L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("archived")
	private Boolean archived;

	@JsonProperty("released")
	private Boolean released;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("releaseDate")
	private String releaseDate;

	@JsonProperty("userStartDate")
	private String userStartDate;

	@JsonProperty("userReleaseDate")
	private String userReleaseDate;

	@JsonProperty("project")
	private String project;

	@JsonProperty("projectId")
	private int projectId;

}