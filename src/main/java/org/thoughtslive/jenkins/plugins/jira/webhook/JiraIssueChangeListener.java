package org.thoughtslive.jenkins.plugins.jira.webhook;

public interface JiraIssueChangeListener {
  /**
   * Return the issue observed by this listener
   * 
   * @return Issue key or id
   */
  String getIssueIdOrKey();

  /**
   * Notify issue change
   * 
   * @param field
   *          changed field
   * @param fieldValue
   *          new field value
   * @return true if handled by this listener, false otherwise
   */
  boolean onIssueUpdated(String field, String fieldValue);
}
