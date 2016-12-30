
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

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
public class Version implements Serializable {

	private final static long serialVersionUID = 2195734358892274719L;

	@JsonProperty("id")
	public String id;

	@JsonProperty("name")
	public String name;

	@JsonProperty("description")
	public String description;

	@JsonProperty("archived")
	public Boolean archived;

	@JsonProperty("released")
	public Boolean released;

	@JsonProperty("startDate")
	public DateTime startDate;

	@JsonProperty("releaseDate")
	public DateTime releaseDate;

	@JsonProperty("userStartDate")
	public DateTime userStartDate;

	@JsonProperty("userReleaseDate")
	public DateTime userReleaseDate;

	@JsonProperty("project")
	public String project;

	@JsonProperty("projectId")
	public int projectId;

}