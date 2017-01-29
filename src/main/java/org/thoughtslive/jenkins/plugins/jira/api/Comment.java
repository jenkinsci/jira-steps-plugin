
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
public class Comment implements Serializable {

	private static final long serialVersionUID = -9114809641205019687L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("author")
	private User author;

	@JsonProperty("body")
	private String body;

	@JsonProperty("updateAuthor")
	private User updateAuthor;

	@JsonProperty("created")
	private DateTime created;

	@JsonProperty("updated")
	private DateTime updated;

}
