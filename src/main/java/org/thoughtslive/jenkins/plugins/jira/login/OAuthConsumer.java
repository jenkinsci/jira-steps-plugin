package org.thoughtslive.jenkins.plugins.jira.login;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import okhttp3.Request;

public class OAuthConsumer extends AbstractOAuthConsumer {

	private static final long serialVersionUID = 4067454013487952351L;

	public OAuthConsumer(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret);
	}

	@Override
	protected HttpRequest wrap(Object request) {
		if (!(request instanceof Request)) {
			throw new IllegalArgumentException("Accepts only requests of type "
					+ Request.class.getCanonicalName());
		}
		return new RequestAdapter((Request) request);
	}

}