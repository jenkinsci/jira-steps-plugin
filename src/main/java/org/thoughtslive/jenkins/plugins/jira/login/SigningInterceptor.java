package org.thoughtslive.jenkins.plugins.jira.login;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.security.ACL;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import jenkins.model.Jenkins;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
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
      if(jiraSite.isUseBearer()) {
        String token = jiraSite.getPassword().getPlainText();
        String encodedHeader =
                "Bearer " + token;
        Request requestWithAuthorization =
                chain.request().newBuilder().addHeader("Authorization", encodedHeader).build();
        return chain.proceed(requestWithAuthorization);
      }
      else {
        String credentials = jiraSite.getUserName() + ":" + jiraSite.getPassword().getPlainText();
        String encodedHeader =
                "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
        Request requestWithAuthorization =
                chain.request().newBuilder().addHeader("Authorization", encodedHeader).build();
        return chain.proceed(requestWithAuthorization);
      }
    } else if (Site.LoginType.OAUTH.name().equalsIgnoreCase(jiraSite.getLoginType())) {
      Request request = chain.request();
      OAuthConsumer consumer =
          new OAuthConsumer(jiraSite.getConsumerKey(), jiraSite.getPrivateKeySecret().getPlainText());
      consumer.setTokenWithSecret(jiraSite.getToken().getPlainText(),
          jiraSite.getSecret().getPlainText());
      consumer.setMessageSigner(new RsaSha1MessageSigner());
      try {
        return chain.proceed((Request) consumer.sign(request).unwrap());
      } catch (OAuthException e) {
        throw new IOException("Error signing request with OAuth.", e);
      }
    } else if (Site.LoginType.CREDENTIAL.name().equalsIgnoreCase(jiraSite.getLoginType())) {
      StandardUsernameCredentials credentialsId = null;
      // credentials is saved in global configuration, there is no context there during test connection so SYSTEM access is used
      credentialsId = CredentialsProvider.lookupCredentials(StandardUsernameCredentials.class,
              Jenkins.get(), ACL.SYSTEM, Collections.emptyList()) //
          .stream() //
          .filter(c -> c.getId().equals(jiraSite.getCredentialsId())) //
          .findFirst() //
          .orElseThrow(() -> new IllegalStateException(Messages.Site_invalidCredentialsId()));
      if(jiraSite.isUseBearer() && credentialsId instanceof UsernamePasswordCredentials) {
        String token = ((UsernamePasswordCredentials)credentialsId).getPassword().getPlainText();
        String encodedHeader =
                "Bearer " + token;
        Request requestWithAuthorization =
                chain.request().newBuilder().addHeader("Authorization", encodedHeader).build();
        return chain.proceed(requestWithAuthorization);
      }
      else {
        String credentials = credentialsId.getUsername();
        if (credentialsId instanceof UsernamePasswordCredentials) {
          credentials +=
                  ":" + ((UsernamePasswordCredentials) credentialsId).getPassword().getPlainText();
        }
        String encodedHeader =
                "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
        Request requestWithAuthorization = chain.request().newBuilder()
                .addHeader("Authorization", encodedHeader).build();
        return chain.proceed(requestWithAuthorization);
      }
    } else {
      throw new IOException("Invalid Login Type, this isn't expected.");
    }
  }

}
