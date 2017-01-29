package org.thoughtslive.jenkins.plugins.jira.login;

import java.io.IOException;
import java.util.Base64;

import org.thoughtslive.jenkins.plugins.jira.Site;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * SigningInterceptor for Retrofit API.
 * 
 * @author Naresh Rayapati
 *
 */
@SuppressFBWarnings
public class SigningInterceptor implements Interceptor {

	private final Site jiraSite;

	public SigningInterceptor(Site jiraSite) {
		this.jiraSite = jiraSite;
	}

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {

		if (jiraSite.getLoginType().equalsIgnoreCase(Site.LoginType.BASIC.name())) {
			String credentials = jiraSite.getUserName() + ":" + jiraSite.getPassword().getPlainText();
			String encodedHeader = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
			Request requestWithAuthorization = chain.request().newBuilder().addHeader("Authorization", encodedHeader).build();
			return chain.proceed(requestWithAuthorization);
		} else if (jiraSite.getLoginType().equalsIgnoreCase(Site.LoginType.OAUTH.name())) {
			Request request = chain.request();
			OAuthConsumer consumer = new OAuthConsumer(jiraSite.getConsumerKey(), jiraSite.getPrivateKey());
			consumer.setTokenWithSecret(jiraSite.getToken().getPlainText(), jiraSite.getSecret().getPlainText());
			consumer.setMessageSigner(new RsaSha1MessageSigner());
			try {
				return chain.proceed((Request) consumer.sign(request).unwrap());
			} catch (OAuthException e) {
				throw new IOException("Error signing request with OAuth.", e);
			}
		} else {
			throw new IOException("Invalid Login Type, this isn't expected.");
		}
	}

}