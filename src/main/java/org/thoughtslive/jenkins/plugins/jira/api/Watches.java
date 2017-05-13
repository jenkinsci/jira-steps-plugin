
package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;
import java.util.List;

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
public class Watches implements Serializable {

  private static final long serialVersionUID = -722010307480344562L;

  @JsonProperty("watchCount")
  private Integer watchCount;

  @JsonProperty("isWatching")
  private Boolean isWatching;

  @JsonProperty("watchers")
  private List<User> watchers;
}
