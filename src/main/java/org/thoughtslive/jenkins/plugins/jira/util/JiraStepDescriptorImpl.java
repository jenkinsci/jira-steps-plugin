package org.thoughtslive.jenkins.plugins.jira.util;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.thoughtslive.jenkins.plugins.jira.Config;
import org.thoughtslive.jenkins.plugins.jira.Site;

import hudson.util.ListBoxModel;

/**
 * Default StepDescriptorImpl for all JIRA steps.
 * 
 * @author Naresh Rayapati
 *
 */
public abstract class JiraStepDescriptorImpl extends AbstractStepDescriptorImpl {

  /**
   * Constructor.
   */
  protected JiraStepDescriptorImpl(Class<? extends StepExecution> executionType) {
    super(executionType);
  }

  /**
   * Fills the site names to the list box.
   * 
   * @return {@link ListBoxModel}
   */
  public ListBoxModel doFillSiteNameItems() {
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

}
