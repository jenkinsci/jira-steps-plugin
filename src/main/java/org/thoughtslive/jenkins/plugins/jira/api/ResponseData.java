package org.thoughtslive.jenkins.plugins.jira.api;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

@Data
@ToString(of = {"successful", "code", "message", "error", "data"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData<T> implements Serializable {

  private static final long serialVersionUID = 7846727738826103343L;

  @Whitelisted
  private boolean successful;

  @Whitelisted
  private int code;

  @Whitelisted
  private String message;

  @Whitelisted
  private String error;

  @Whitelisted
  private T data;
}
