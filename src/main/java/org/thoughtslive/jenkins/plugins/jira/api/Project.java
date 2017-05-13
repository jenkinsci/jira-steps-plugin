
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
public class Project implements Serializable {

  private static final long serialVersionUID = -4217312651288636551L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("key")
  private String key;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("lead")
  private User lead;

  @JsonProperty("components")
  private List<Component> components;

  @JsonProperty("versions")
  private List<Version> versions;

  @JsonProperty("issueTypes")
  private List<IssueType> issueTypes;

  @JsonProperty("projectCategory")
  private ProjectCategory projectCategory;

}
