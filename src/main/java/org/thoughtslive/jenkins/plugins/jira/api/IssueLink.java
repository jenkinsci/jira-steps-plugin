
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
public class IssueLink implements Serializable {

  private static final long serialVersionUID = -6353677854648448215L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private IssueLinkType type;

  @JsonProperty("inwardIssue")
  private Issue inwardIssue;

  @JsonProperty("outwardIssue")
  private Issue outwardIssue;

  @JsonProperty("comment")
  private Comment comment;
}
