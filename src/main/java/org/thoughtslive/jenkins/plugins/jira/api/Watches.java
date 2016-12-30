
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
public class Watches implements Serializable {

	private final static long serialVersionUID = -2578055776913736842L;

	@JsonProperty("self")
	public String self;

	@JsonProperty("watchCount")
	public Integer watchCount;

	@JsonProperty("isWatching")
	public Boolean isWatching;

	@JsonProperty("watchers")
	public User[] watchers;
}
