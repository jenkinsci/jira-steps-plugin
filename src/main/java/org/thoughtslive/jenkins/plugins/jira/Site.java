package org.thoughtslive.jenkins.plugins.jira;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.Secret;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * Represents a configuration needed to access this JIRA.
 *
 * @author Naresh Rayapati
 */
@Log
public class Site extends AbstractDescribableImpl<Site> {

  public enum LoginType {
    BASIC, OAUTH
  }

  @Getter
  private final String name;
  @Getter
  private final URL url;
  @Getter
  private final String loginType;
  @Getter
  private int timeout;

  // Basic
  @Getter
  @Setter(onMethod = @__({@DataBoundSetter}))
  private String userName;
  @Getter
  private Secret password;

  // OAuth
  @Getter
  @Setter(onMethod = @__({@DataBoundSetter}))
  private String consumerKey;
  @Getter
  @Setter(onMethod = @__({@DataBoundSetter}))
  private String privateKey;
  @Getter
  private Secret secret;
  @Getter
  private Secret token;

  private transient JiraService jiraService = null;

  @DataBoundConstructor
  public Site(final String name, final URL url, final String loginType, final int timeout) {

    this.name = Util.fixEmpty(name);
    this.url = url;
    this.loginType = Util.fixEmpty(loginType);
    this.timeout = timeout;
  }

  public String isLoginType(String loginType) {
    return this.loginType.equalsIgnoreCase(loginType) ? "true" : "";
  }

  @DataBoundSetter
  public void setPassword(final String password) {
    this.password = Secret.fromString(Util.fixEmpty(password));
  }

  @DataBoundSetter
  public void setSecret(final String secret) {
    this.secret = Secret.fromString(Util.fixEmpty(secret));
  }

  @DataBoundSetter
  public void setToken(final String token) {
    this.token = Secret.fromString(Util.fixEmpty(token));
  }

  public static Site get(final String siteName) {
    Site[] sites = Config.DESCRIPTOR.getSites();
    for (Site site : sites) {
      if (site.getName().equalsIgnoreCase(siteName)) {
        return site;
      }
    }
    return null;
  }

  public JiraService getService() {
    if (jiraService == null) {
      return new JiraService(this);
    }
    return jiraService;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<Site> {
    @Override
    public String getDisplayName() {
      return "JIRA Steps: Site";
    }

    /**
     * Checks if the details required for the basic login is valid. TODO: This validation can be
     * moved to Config so that we can also verify the name is valid.
     */
    public FormValidation doValidateBasic(@QueryParameter String name, @QueryParameter String url,
        @QueryParameter String loginType, @QueryParameter String timeout,
        @QueryParameter String userName, @QueryParameter String password,
        @QueryParameter String consumerKey, @QueryParameter String privateKey,
        @QueryParameter String secret, @QueryParameter String token) throws IOException {
      url = Util.fixEmpty(url);
      name = Util.fixEmpty(name);
      userName = Util.fixEmpty(userName);
      password = Util.fixEmpty(password);
      URL mainURL = null;

      if (name == null) {
        return FormValidation.error("Name is empty or null.");
      }

      try {
        if (url == null) {
          return FormValidation.error("No URL given.");
        }
        mainURL = new URL(url);
      } catch (MalformedURLException e) {
        return FormValidation.error(String.format("Malformed URL (%s)", url), e);
      }

      int t = 0;
      try {
        t = Integer.parseInt(timeout);
        if (t <= 100) {
          return FormValidation.error("Timeout can't be lessthan 100.");
        }
      } catch (NumberFormatException e) {
        return FormValidation.error("Timeout is not a number");
      }

      Site site = new Site(name, mainURL, "BASIC", t);

      if (userName == null) {
        return FormValidation.error("UserName is empty or null.");
      }
      if (password == null) {
        return FormValidation.error("Password is empty or null.");
      }
      site.setUserName(userName);
      site.setPassword(password);

      try {
        final JiraService service = new JiraService(site);
        final ResponseData<Map<String, Object>> response = service.getServerInfo();
        if (response.isSuccessful()) {
          return FormValidation.ok("Success: " + response.getData().get("serverTitle") + " - "
              + response.getData().get("version"));
        } else {
          return FormValidation.error(response.getError());
        }
      } catch (Exception e) {
        log.log(Level.WARNING, "Failed to Basic login to JIRA at " + url, e);
      }
      return FormValidation.error("Failed to Basic login to JIRA: " + url);
    }

    // This is stupid but no choice as I couldn't find the way to get the
    // value loginType (radioBlock as a @QueryParameter)
    public FormValidation doValidateOAuth(@QueryParameter String name, @QueryParameter String url,
        @QueryParameter String loginType, @QueryParameter String timeout,
        @QueryParameter String userName, @QueryParameter String password,
        @QueryParameter String consumerKey, @QueryParameter String privateKey,
        @QueryParameter String secret, @QueryParameter String token) throws IOException {
      url = Util.fixEmpty(url);
      name = Util.fixEmpty(name);
      consumerKey = Util.fixEmpty(consumerKey);
      privateKey = Util.fixEmpty(privateKey);
      secret = Util.fixEmpty(secret);
      token = Util.fixEmpty(token);
      URL mainURL = null;

      if (name == null) {
        return FormValidation.error("Name is empty.");
      }

      try {
        if (url == null) {
          return FormValidation.error("No URL given.");
        }
        mainURL = new URL(url);
      } catch (MalformedURLException e) {
        return FormValidation.error(String.format("Malformed URL (%s)", url), e);
      }

      int t = 0;
      try {
        t = Integer.parseInt(timeout);
        if (t <= 100) {
          return FormValidation.error("Timeout can't be lessthan 100.");
        }
      } catch (NumberFormatException e) {
        return FormValidation.error("Timeout is not a number");
      }

      Site site = new Site(name, mainURL, "OAUTH", t);

      if (consumerKey == null) {
        return FormValidation.error("Consumer Key is empty or null.");
      }
      if (privateKey == null) {
        return FormValidation.error("Private Key is empty or null.");
      }
      if (secret == null) {
        return FormValidation.error("Secret is empty or null.");
      }
      if (token == null) {
        return FormValidation.error("Token is empty or null.");
      }
      site.setConsumerKey(consumerKey);
      site.setPrivateKey(privateKey);
      site.setSecret(secret);
      site.setToken(token);

      try {
        final JiraService service = new JiraService(site);
        final ResponseData<Map<String, Object>> response = service.getServerInfo();
        if (response.isSuccessful()) {
          return FormValidation.ok("Success: " + response.getData().get("serverTitle") + " - "
              + response.getData().get("version"));
        } else {
          return FormValidation.error(response.getError());
        }
      } catch (Exception e) {
        log.log(Level.WARNING, "Failed to OAuth login to JIRA at " + url, e);
      }
      return FormValidation.error("Failed to OAuth login to JIRA: " + url);
    }
  }
}
