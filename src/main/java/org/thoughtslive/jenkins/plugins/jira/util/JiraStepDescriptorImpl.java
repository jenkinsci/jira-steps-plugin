package org.thoughtslive.jenkins.plugins.jira.util;

import com.google.common.collect.ImmutableSet;
import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import java.util.Set;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.thoughtslive.jenkins.plugins.jira.Config;
import org.thoughtslive.jenkins.plugins.jira.Site;

/**
 * Default StepDescriptorImpl for all JIRA steps.
 *
 * @author Naresh Rayapati
 */
public abstract class JiraStepDescriptorImpl extends StepDescriptor {

  /**
   * Fills the site names to the list box.
   *
   * @return {@link ListBoxModel}
   */
  public ListBoxModel doFillSiteItems() {
    ListBoxModel list = new ListBoxModel();
    list.add("Please select", "");
    for (Site site : Config.DESCRIPTOR.getSites()) {
      list.add(site.getName());
    }
    return list;
  }

  protected String getPrefix() {
    return "JIRA Steps: ";
  }

  @Override
  public Set<? extends Class<?>> getRequiredContext() {
    return ImmutableSet.of(Run.class, TaskListener.class, EnvVars.class);
  }
}
