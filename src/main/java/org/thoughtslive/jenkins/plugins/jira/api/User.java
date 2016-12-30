
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

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
public class User implements Serializable {
	private final static long serialVersionUID = -3553835852025983698L;

	@JsonProperty("self")
	public String self;

	@JsonProperty("name")
	public String name;

	@JsonProperty("key")
	public String key;

	@JsonProperty("emailAddress")
	public String emailAddress;

	@JsonProperty("displayName")
	public String displayName;

	@JsonProperty("active")
	public Boolean active;

	@JsonProperty("timeZone")
	public String timeZone;

}
