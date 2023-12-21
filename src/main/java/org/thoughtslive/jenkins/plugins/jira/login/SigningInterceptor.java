package org.thoughtslive.jenkins.plugins.jira.login;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.security.ACL;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.stream.Stream;

import jenkins.model.Jenkins;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import org.thoughtslive.jenkins.plugins.jira.Messages;
import org.thoughtslive.jenkins.plugins.jira.Site;

/**
 * SigningInterceptor for Retrofit API.
 *
 * @author Naresh Rayapati
 */
@SuppressFBWarnings
public class SigningInterceptor implements Interceptor {

  private final Site jiraSite;

  public SigningInterceptor(Site jiraSite) {
    this.jiraSite = jiraSite;
  }

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {

    if (Site.LoginType.BASIC.name().equalsIgnoreCase(jiraSite.getLoginType())) {
      String credentials = jiraSite.getUserName() + ":" + jiraSite.getPassword().getPlainText();
      String encodedHeader =
          "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
      Request requestWithAuthorization =
          chain.request().newBuilder().addHeader("Authorization", encodedHeader).build();
      return chain.proceed(requestWithAuthorization);
    } else if (Site.LoginType.OAUTH.name().equalsIgnoreCase(jiraSite.getLoginType())) {
      Request request = chain.request();
      OAuthConsumer consumer =
          new OAuthConsumer(jiraSite.getConsumerKey(), jiraSite.getPrivateKey());
      consumer.setTokenWithSecret(jiraSite.getToken().getPlainText(),
          jiraSite.getSecret().getPlainText());
      consumer.setMessageSigner(new RsaSha1MessageSigner());
      try {
        return chain.proceed((Request) consumer.sign(request).unwrap());
      } catch (OAuthException e) {
        throw new IOException("Error signing request with OAuth.", e);
      }
    } else if (Site.LoginType.CREDENTIAL.name().equalsIgnoreCase(jiraSite.getLoginType())) {
      StandardCredentials credentials = null;
      // credentials is saved in global configuration, there is no context there during test connection so SYSTEM access is used
      credentials = Stream.concat(
          CredentialsProvider.lookupCredentials(StandardCredentials.class, Jenkins.get(), ACL.SYSTEM, Collections.emptyList()) //
              .stream(),
          CredentialsProvider.lookupCredentials(StringCredentials.class, Jenkins.get(), ACL.SYSTEM, Collections.emptyList()) //
              .stream()
          )
          .filter(c -> c.getId().equals(jiraSite.getCredentialsId())) //
          .findFirst() //
          .orElseThrow(() -> new IllegalStateException(Messages.Site_invalidCredentialsId()));
      final String encodedHeader;
      if (credentials instanceof UsernamePasswordCredentials) {
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) credentials;
        String username = usernamePasswordCredentials.getUsername();
        String password = usernamePasswordCredentials.getPassword().getPlainText();
        String userPass = username + ":" + password;
        encodedHeader = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
      } else if (credentials instanceof StringCredentialsImpl) {
        StringCredentialsImpl stringCredentials = (StringCredentialsImpl) credentials;
        encodedHeader = "Bearer " + stringCredentials.getSecret().getPlainText();
      } else {
        throw new IllegalArgumentException(String.format(
            "Credentials %s has unsupported type %s. Only UsernamePasswordCredentials and StringCredentials are supported.",
            jiraSite.getCredentialsId(), credentials.getClass().getCanonicalName()
        ));
      }
      Request requestWithAuthorization = chain.request().newBuilder()
          .addHeader("Authorization", encodedHeader).build();
      return chain.proceed(requestWithAuthorization);
    } else {
      throw new IOException("Invalid Login Type, this isn't expected.");
    }
  }

}
