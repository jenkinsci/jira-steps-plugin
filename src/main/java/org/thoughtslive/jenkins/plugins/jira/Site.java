package org.thoughtslive.jenkins.plugins.jira;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import hudson.Extension;
import hudson.Util;
import hudson.diagnosis.OldDataMonitor;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Queue;
import hudson.model.queue.Tasks;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import hudson.util.XStream2;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.acegisecurity.Authentication;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.service.JiraService;

/**
 * Represents a configuration needed to access this JIRA.
 *
 * @author Naresh Rayapati
 */
@Log
public class Site extends AbstractDescribableImpl<Site> {

    @Getter
    private final String name;

    @Getter
    private final URL url;

    @Getter
    private final String loginType;

    @Getter
    private int timeout;

    @Getter
    private int readTimeout;
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
    private transient String privateKey;

    @Getter
    private Secret privateKeySecret;

    @Getter
    private Secret secret;

    @Getter
    private Secret token;
    // Credentials plugin
    @Getter
    @Setter(onMethod = @__({@DataBoundSetter}))
    private String credentialsId;

    private transient JiraService jiraService = null;

    @DataBoundConstructor
    public Site(final String name, final URL url, final String loginType, final int timeout) {
        this.name = Util.fixEmpty(name);
        this.url = url;
        this.loginType = Util.fixEmpty(loginType);
        this.timeout = timeout;
    }

    public static Site get(final String siteName) {
        Site[] sites = JiraStepsConfig.DESCRIPTOR.getSites();
        for (Site site : sites) {
            if (site.getName().equalsIgnoreCase(siteName)) {
                return site;
            }
        }
        return null;
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
    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @DataBoundSetter
    public void setToken(final String token) {
        this.token = Secret.fromString(Util.fixEmpty(token));
    }

    @DataBoundSetter
    public void setPrivateKeySecret(final String privateKey) {
        this.privateKeySecret = Secret.fromString(Util.fixEmpty(privateKey));
    }

    public JiraService getService() {
        if (jiraService == null) {
            this.jiraService = new JiraService(this);
        }
        return jiraService;
    }

    public enum LoginType {
        BASIC,
        OAUTH,
        CREDENTIAL
    }

    @Restricted(DoNotUse.class)
    public static class ConverterImpl extends XStream2.PassthruConverter<Site> {
        public ConverterImpl(XStream2 xstream) {
            super(xstream);
        }

        @Override
        protected void callback(Site site, UnmarshallingContext context) {
            String privateKey = site.getPrivateKey();
            if (privateKey != null && !privateKey.isEmpty()) {
                site.setPrivateKeySecret(privateKey);
                OldDataMonitor.report(context, "2.361.1");
            }
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Site> {

        @Override
        public String getDisplayName() {
            return "JIRA Steps: Site";
        }

        public FormValidation doCheckName(final @QueryParameter String name) {
            if (StringUtils.isBlank(name)) {
                return FormValidation.error(Messages.required());
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckUrl(final @QueryParameter String url) {
            if (StringUtils.isBlank(url)) {
                return FormValidation.error(Messages.required());
            }
            try {
                new URL(url);
                return FormValidation.ok();
            } catch (MalformedURLException e) {
                return FormValidation.error(e.getMessage());
            }
        }

        public FormValidation doCheckTimeout(final @QueryParameter Integer timeout) {
            if (timeout == null || timeout <= 100) {
                return FormValidation.error(Messages.Site_invalidTimeout());
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckReadTimeout(final @QueryParameter Integer readTimeout) {
            if (readTimeout == null || readTimeout <= 100) {
                return FormValidation.error(Messages.Site_invalidReadTimeout());
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckCredentialsId(
                @AncestorInPath Item item,
                final @QueryParameter String credentialsId,
                final @QueryParameter String url) {

            if (item == null) {
                if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                    return FormValidation.ok();
                }
            } else if (!item.hasPermission(Item.EXTENDED_READ) && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
                return FormValidation.ok();
            }
            if (StringUtils.isBlank(credentialsId)) {
                return FormValidation.warning(Messages.Site_emptyCredentialsId());
            }

            List<DomainRequirement> domainRequirements =
                    URIRequirementBuilder.fromUri(url).build();
            if (CredentialsProvider.listCredentials(
                            StandardUsernameCredentials.class,
                            item,
                            getAuthentication(item),
                            domainRequirements,
                            CredentialsMatchers.withId(credentialsId))
                    .isEmpty()) {
                return FormValidation.error(Messages.Site_invalidCredentialsId());
            }
            return FormValidation.ok();
        }

        public ListBoxModel doFillCredentialsIdItems(
                final @AncestorInPath Item item,
                @QueryParameter String credentialsId,
                final @QueryParameter String url) {

            StandardListBoxModel result = new StandardListBoxModel();

            credentialsId = StringUtils.trimToEmpty(credentialsId);
            if (item == null) {
                if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                    return result.includeCurrentValue(credentialsId);
                }
            } else {
                if (!item.hasPermission(Item.EXTENDED_READ) && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
                    return result.includeCurrentValue(credentialsId);
                }
            }

            Authentication authentication = getAuthentication(item);
            List<DomainRequirement> domainRequirements =
                    URIRequirementBuilder.fromUri(url).build();
            CredentialsMatcher always = CredentialsMatchers.always();
            Class type = UsernamePasswordCredentials.class;

            result.includeEmptyValue();
            if (item != null) {
                result.includeMatchingAs(authentication, item, type, domainRequirements, always);
            } else {
                result.includeMatchingAs(authentication, Jenkins.get(), type, domainRequirements, always);
            }
            return result;
        }

        protected Authentication getAuthentication(Item item) {
            return item instanceof Queue.Task ? Tasks.getAuthenticationOf((Queue.Task) item) : ACL.SYSTEM;
        }

        @POST
        public FormValidation doValidateCredentials(
                @QueryParameter String url,
                @QueryParameter String credentialsId,
                @QueryParameter Integer timeout,
                @QueryParameter Integer readTimeout)
                throws IOException {
            if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                return FormValidation.warning("Insufficient permissions");
            }

            FormValidation validation = doCheckUrl(url);
            if (validation.kind != Kind.OK) {
                return FormValidation.error(Messages.Site_emptyURL());
            }

            validation = doCheckTimeout(timeout);
            if (validation.kind != Kind.OK) {
                return validation;
            }

            validation = doCheckReadTimeout(readTimeout);
            if (validation.kind != Kind.OK) {
                return validation;
            }

            Site site = new Site("test", new URL(url), LoginType.CREDENTIAL.name(), timeout);
            site.setCredentialsId(credentialsId);
            site.setReadTimeout(readTimeout);

            try {
                final JiraService service = new JiraService(site);
                final ResponseData<Map<String, Object>> response = service.getServerInfo();
                if (response.isSuccessful()) {
                    Map<String, Object> data = response.getData();
                    return FormValidation.ok(Messages.Site_testSuccess(data.get("serverTitle"), data.get("version")));
                } else {
                    return FormValidation.error(response.getError());
                }
            } catch (Exception e) {
                log.log(Level.WARNING, Messages.Site_testFail(url), e);
            }
            return FormValidation.error(Messages.Site_testFail(url));
        }

        /**
         * Checks if the details required for the basic login is valid. TODO: This validation can be
         * moved to JiraStepsConfig so that we can also verify the name is valid.
         */
        @POST
        public FormValidation doValidateBasic(
                @QueryParameter String name,
                @QueryParameter String url,
                @QueryParameter String loginType,
                @QueryParameter String timeout,
                @QueryParameter String readTimeout,
                @QueryParameter String userName,
                @QueryParameter String password,
                @QueryParameter String consumerKey,
                @QueryParameter String privateKey,
                @QueryParameter String secret,
                @QueryParameter String token)
                throws IOException {
            if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                return FormValidation.warning("Insufficient permissions");
            }

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
                    return FormValidation.error("Timeout can't be less than 100.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Timeout is not a number");
            }

            int rt = 0;
            try {
                rt = Integer.parseInt(readTimeout);
                if (rt <= 100) {
                    return FormValidation.error("Read Timeout can't be less than 100.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Read Timeout is not a number");
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
            site.setReadTimeout(rt);

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
        @POST
        public FormValidation doValidateOAuth(
                @QueryParameter String name,
                @QueryParameter String url,
                @QueryParameter String loginType,
                @QueryParameter String timeout,
                @QueryParameter String readTimeout,
                @QueryParameter String userName,
                @QueryParameter String password,
                @QueryParameter String consumerKey,
                @QueryParameter String privateKey,
                @QueryParameter String secret,
                @QueryParameter String token)
                throws IOException {
            if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                return FormValidation.warning("Insufficient permissions");
            }

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
                    return FormValidation.error("Timeout can't be less than 100.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Timeout is not a number");
            }

            int rt = 0;
            try {
                rt = Integer.parseInt(readTimeout);
                if (rt <= 100) {
                    return FormValidation.error("Read Timeout can't be less than 100.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Read Timeout is not a number");
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
            site.setPrivateKeySecret(privateKey);
            site.setSecret(secret);
            site.setToken(token);
            site.setReadTimeout(rt);

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
