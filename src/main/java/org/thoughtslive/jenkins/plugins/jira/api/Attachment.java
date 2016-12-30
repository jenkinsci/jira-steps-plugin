
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
public class Attachment implements Serializable {

	private final static long serialVersionUID = 3112598371329647518L;

	@JsonProperty("self")
	public String self;

	@JsonProperty("id")
	public String id;

	@JsonProperty("filename")
	public String filename;

	@JsonProperty("author")
	public User author;

	@JsonProperty("created")
	public DateTime created;

	@JsonProperty("size")
	public Integer size;

	@JsonProperty("mimeType")
	public String mimeType;

	@JsonProperty("content")
	public String content;

	@JsonProperty("thumbnail")
	public String thumbnail;

}
