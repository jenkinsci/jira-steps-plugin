
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
public class Issue implements Serializable {
	private final static long serialVersionUID = 7689184735166991068L;

	@JsonProperty("id")
	public String id;

	@JsonProperty("self")
	public String self;

	@JsonProperty("key")
	public String key;

	@JsonProperty("fields")
	public Fields fields;

	@JsonProperty("transition")
	public Transition transition = null;

}
