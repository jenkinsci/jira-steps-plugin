package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;

import org.joda.time.DateTime;
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
public class LoginInfo implements Serializable {

  private static final long serialVersionUID = 850858298837619649L;

  @JsonProperty("failedLoginCount")
  private int failedLoginCount;

  @JsonProperty("loginCount")
  private int loginCount;

  @JsonProperty("lastFailedLoginDate")
  private DateTime lastFailedLoginDate;

  @JsonProperty("previousLoginDate")
  private DateTime previousLoginDate;
}
