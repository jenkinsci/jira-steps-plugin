package org.thoughtslive.jenkins.plugins.jira.login;

import com.google.api.client.auth.oauth.OAuthRsaSigner;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SignatureBaseString;
import java.util.Base64;

public class RsaSha1MessageSigner extends OAuthMessageSigner {

  private static final long serialVersionUID = -1777278892400172839L;

  @Override
  public String getSignatureMethod() {
    return "RSA-SHA1";
  }

  @Override
  public String sign(HttpRequest request, HttpParameters requestParams)
      throws OAuthMessageSignerException {

    final OAuthRsaSigner signer = new OAuthRsaSigner();
    final byte[] privateBytes = Base64.getDecoder().decode(getConsumerSecret());
    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);

    try {
      signer.privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
      final String signatureBaseString = new SignatureBaseString(request, requestParams).generate();
      return signer.computeSignature(signatureBaseString);
    } catch (GeneralSecurityException e) {
      throw new OAuthMessageSignerException(e);
    }
  }
}
