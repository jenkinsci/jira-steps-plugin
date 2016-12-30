
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
public class Comment implements Serializable {

	private final static long serialVersionUID = 6138170662415305859L;

	@JsonProperty("self")
	public String self;

	@JsonProperty("id")
	public String id;

	@JsonProperty("author")
	public User author;

	@JsonProperty("body")
	public String body;

	@JsonProperty("updateAuthor")
	public User updateAuthor;

	@JsonProperty("created")
	public DateTime created;

	@JsonProperty("updated")
	public DateTime updated;

}
