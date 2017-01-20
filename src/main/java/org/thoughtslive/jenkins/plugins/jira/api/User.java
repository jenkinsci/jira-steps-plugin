
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
public class User implements Serializable {
	private final static long serialVersionUID = -3553835852025983698L;

	@JsonProperty("self")
	private String self;

	@JsonProperty("name")
	private String name;

	@JsonProperty("key")
	private String key;

	@JsonProperty("emailAddress")
	private String emailAddress;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("active")
	private Boolean active;

	@JsonProperty("timeZone")
	private String timeZone;

}
