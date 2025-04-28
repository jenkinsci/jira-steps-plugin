package org.thoughtslive.jenkins.plugins.jira;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.CopyOnWriteList;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nonnull;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.Converter;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest2;

/**
 * Represents JIRA Global Configuration.
 *
 * @author Naresh Rayapati
 */
@SuppressFBWarnings
public class JiraStepsConfig extends AbstractDescribableImpl<JiraStepsConfig> {

  @Extension
  public static final ConfigDescriptorImpl DESCRIPTOR = new ConfigDescriptorImpl();
  public final String siteName;

  @DataBoundConstructor
  public JiraStepsConfig(String siteName) {
    if (siteName == null) {
      Site[] sites = DESCRIPTOR.getSites();
      if (sites.length > 0) {
        siteName = sites[0].getName();
      }
    }
    this.siteName = siteName;
  }

  public Site getSite() {
    Site[] sites = DESCRIPTOR.getSites();
    if (siteName == null && sites.length > 0) {
      // default
      return sites[0];
    }

    for (Site site : sites) {
      if (site.getName().equals(siteName)) {
        return site;
      }
    }
    return null;
  }

  @Override
  public ConfigDescriptorImpl getDescriptor() {
    return DESCRIPTOR;
  }

  public static final class ConfigDescriptorImpl extends Descriptor<JiraStepsConfig>
      implements Serializable {

    private static final long serialVersionUID = 6174559183832237318L;
    private final CopyOnWriteList<Site> sites = new CopyOnWriteList<Site>();

    public ConfigDescriptorImpl() {
      super(JiraStepsConfig.class);
      load();
    }

    @Override
    public String getDisplayName() {
      return "JIRA Steps: JiraStepsConfig";
    }

    public Site[] getSites() {
      return sites.toArray(new Site[0]);
    }

    public void setSites(Site[] newSites) {
      sites.replaceBy(newSites);
    }

    @Override
    public JiraStepsConfig newInstance(@Nonnull final StaplerRequest2 req, final JSONObject formData)
        throws FormException {
      JiraStepsConfig jiraConfig = req.bindJSON(JiraStepsConfig.class, formData);
      if (jiraConfig.siteName == null) {
        jiraConfig = null;
      }
      return jiraConfig;
    }

    @Override
    public boolean configure(StaplerRequest2 req, JSONObject formData) {
      Stapler.CONVERT_UTILS.deregister(java.net.URL.class);
      Stapler.CONVERT_UTILS.register(new EmptyFriendlyURLConverter(), java.net.URL.class);
      sites.replaceBy(req.bindJSONToList(Site.class, formData.get("sites")));
      save();
      return true;
    }

    @Restricted(NoExternalUse.class)
    public static class EmptyFriendlyURLConverter implements Converter {

      @Override
      public Object convert(@SuppressWarnings("rawtypes") Class aClass, Object o) {
        if (o == null || "".equals(o) || "null".equals(o)) {
          return null;
        }
        try {
          return new URL(o.toString());
        } catch (MalformedURLException e) {
          return null;
        }
      }
    }
  }
}
