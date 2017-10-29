
package org.thoughtslive.jenkins.plugins.jira.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.util.CustomDateTimeDeserializer;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Attachment implements Serializable {

  private static final long serialVersionUID = -6603159858268979491L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("filename")
  private String filename;

  @JsonProperty("author")
  private User author;

  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  @JsonProperty("created")
  private DateTime created;

  @JsonProperty("size")
  private Integer size;

  @JsonProperty("mimeType")
  private String mimeType;

  @JsonProperty("content")
  private String content;
}
