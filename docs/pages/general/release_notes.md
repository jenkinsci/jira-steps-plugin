---
title: Release notes
tags: [getting_started]
keywords: release notes, announcements, what's new, new features
summary: "Change log."
sidebar: jira_sidebar
permalink: release_notes.html
---
* ## **1.1.0**
  * <span style="color:red">Upgrading to this version will break few existing steps, please read the following notes for more information.</span>.
  * Multiple Fixes: [#29](https://github.com/jenkinsci/jira-steps-plugin/issues/29) Make `id` type consistent across and other minor fixes.
    * Make `id` type consistent (to `String`) across all the objects.
    * `jiraNewIssue` and `jiraNewIssues`
      * Allow issue type look up by name (`fields->issuetype->name`)
      * Allow project look up by key (`fields->project->key`)
    * `@ToString` is duplicate in all api objects (`@Data` should take care of
it already.)
    * `Array` type can be just either `List` or `Set`. Changed across all objects. No need of as String[] ans more. Ex: `['a','b'] as String[]` is now just `['a', 'b']`.
    * `fields` variable is in the wrong position for `Transitions`.
    * Updated documentation accordingly.
  * Added Terms and Conditions - Google Analytics. 

* ## **1.0.3**
  * Fix: [#15](https://github.com/jenkinsci/jira-steps-plugin/issues/15) - Serialization error while querying component using getComponent.
  * Enhancement: [#17](https://github.com/jenkinsci/jira-steps-plugin/issues/17) - Expose access to the parent node for issue.
* ## **1.0.2**
  * Documentation update. No functional change.
* ## **1.0.1**
  * Fix: [#3](https://github.com/jenkinsci/jira-steps-plugin/issues/3) - Error editing issue with existing fix version.
  * More documentation.
* ## **1.0.0**
  * Initial release.
  * Manual tested with JIRA API Version [6.4.13](https://docs.atlassian.com/jira/REST/6.4.13/).
  * Steps to Support
    * Component
      * jiraGetComponent
      * jiraNewComponent
      * jiraEditComponent
      * jiraGetComponentIssueCount
    * Issue
      * jiraGetIssue
      * jiraNewIssue
      * jiraNewIssues
      * jiraEditIssue
      * jiraAssignIssue
    * Comments
      * jiraGetComments
      * jiraAddComment
      * jiraEditComment
      * jiraGetComment
    * Email
      * jiraNotifyIssue
    * Transitions
      * jiraGetTransitions
      * jiraTransitionIssue
    * Watchers
      * jiraGetWatches
      * jiraAddWatcher
    * Project
      * jiraGetProjects
      * jiraGetProject
    * Versions
      * jiraGetVersion
      * jiraNewVersion
      * jiraEditVersion
    * IssueLinks
      * jiraLinkIssues
    * IssueLinkTypes
      * jiraGetIssueLinkTypes
    * Search
      * jiraJqlSearch

{% include links.html %}
