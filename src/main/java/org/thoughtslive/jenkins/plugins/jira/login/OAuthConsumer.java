package org.thoughtslive.jenkins.plugins.jira.login;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import okhttp3.Request;

import java.io.Serial;

public class OAuthConsumer extends AbstractOAuthConsumer {

  @Serial
  private static final long serialVersionUID = 1364436370216401109L;

  public OAuthConsumer(String consumerKey, String consumerSecret) {
    super(consumerKey, consumerSecret);
  }

  @Override
  protected HttpRequest wrap(Object request) {
    if (!(request instanceof Request)) {
      throw new IllegalArgumentException(
          "Accepts only requests of type " + Request.class.getCanonicalName());
    }
    return new RequestAdapter((Request) request);
  }

}
