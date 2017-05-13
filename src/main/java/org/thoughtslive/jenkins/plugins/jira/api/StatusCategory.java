
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

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class StatusCategory implements Serializable {

  private static final long serialVersionUID = 981603626054788439L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("key")
  private String key;

  @JsonProperty("colorName")
  private String colorName;

  @JsonProperty("name")
  private String name;

}
