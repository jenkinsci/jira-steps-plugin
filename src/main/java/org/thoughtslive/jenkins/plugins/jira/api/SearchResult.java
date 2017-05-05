package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;

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
public class SearchResult implements Serializable {

  private static final long serialVersionUID = -7424319344875196054L;

  @JsonProperty("jql")
  private String jql;

  @JsonProperty("startAt")
  private int startAt;

  @JsonProperty("maxResults")
  private int maxResults;

  @JsonProperty("total")
  private int total;

  @JsonProperty("issues")
  private List<BasicIssue> issues;
}
