
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
public class Component implements Serializable {

  private static final long serialVersionUID = -6567966032642998244L;

  // NOT VALID while sending this as Input.
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("leadUserName")
  private String leadUserName;

  // NOT VALID while sending this as Input.
  @JsonProperty("realAssignee")
  private User realAssignee;

  @JsonProperty("assigneeType")
  private String userType;

  @JsonProperty("isAssigneeTypeValid")
  private boolean isAssigneeTypeValid;

  @JsonProperty("project")
  private String project;

  @JsonProperty("projectId")
  private int projectId;

}
