
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
public class EmailTo implements Serializable {

  private static final long serialVersionUID = 1986209333454558972L;

  @JsonProperty("reporter")
  private Boolean reporter;

  @JsonProperty("assignee")
  private Boolean assignee;

  @JsonProperty("watchers")
  private Boolean watchers;

  @JsonProperty("voters")
  private Boolean voters;

  @JsonProperty("users")
  private List<User> users = null;

  @JsonProperty("groups")
  private List<Group> groups = null;
}
