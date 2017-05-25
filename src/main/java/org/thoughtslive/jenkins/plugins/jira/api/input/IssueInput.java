package org.thoughtslive.jenkins.plugins.jira.api.input;

import java.io.Serializable;
import java.util.Map;

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
public class IssueInput implements Serializable {

  private static final long serialVersionUID = 536532573196612271L;

  @JsonProperty("update")
  private Map<String, Object> update;

  @JsonProperty("fields")
  private Map<String, Object> fields;

}
