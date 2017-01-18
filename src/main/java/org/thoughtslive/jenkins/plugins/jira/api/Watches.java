
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({ @DataBoundConstructor }))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings
public class Watches implements Serializable {

	private final static long serialVersionUID = -2578055776913736842L;

	@JsonProperty("self")
	private String self;

	@JsonProperty("watchCount")
	private Integer watchCount;

	@JsonProperty("isWatching")
	private Boolean isWatching;

	@JsonProperty("watchers")
	private User[] watchers;
}
