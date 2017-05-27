---
title: Release notes
tags: [getting_started]
keywords: release notes, announcements, what's new, new features
summary: "Change log."
sidebar: jira_sidebar
permalink: release_notes.html
---
* ## **1.2.0** (Unreleased)
  * [JENKINS-44254](https://issues.jenkins-ci.org/browse/JENKINS-44254) Ability to retrieve and update custom fields.
    * All Steps: No restrictions on what(fields) can be sent to JIRA and retrieve from JIRA. Every field can either be int or string.
  * [JENKINS-44460](https://issues.jenkins-ci.org/browse/JENKINS-44460) No DataBoundConstructor on joda.time.DateTime.
  * [JENKINS-44400](https://issues.jenkins-ci.org/browse/JENKINS-44400) Add more steps related to Projects.
    * jiraGetProjectVersions.
    * jiraGetProjectComponents.
    * jiraGetProjectStatuses.
* ## **1.1.0**
  * <span style="color:red">Upgrading to this version will break few existing steps, please read the following notes for more information.</span>.
  * Multiple Fixes: [#29](https://github.com/jenkinsci/jira-steps-plugin/issues/29) Make `id` type consistent across and other minor fixes.
    * Made `id` type consistent (to `String`) across all the objects. (Non Passive change, existing code need to be changed to use Strings, this is applicable for mostly all the steps).
    * Changes to `jiraNewIssue` and `jiraNewIssues`.
      * Allow issue type look up by name (`fields->issuetype->name`).
      * Allow project look up by key (`fields->project->key`).
    * `@ToString` is duplicate in all api objects (`@Data` should take care of it already.)
    * `Array` type can be just either `List` or `Set`. Changed across all objects. No need of as String[] anymore. Ex: `['a','b'] as String[]` is now just `['a', 'b']`. (Non Passive change).
    * `fields` variable is in the wrong position for `Transitions`.
    * Updated documentation accordingly.
  * [#28](https://github.com/jenkinsci/jira-steps-plugin/issues/28) ISSUE-28 Enable more fields while creating/updating issues.
  * Added Terms and Conditions - Google Analytics.
  * [JENKINS-44252](https://issues.jenkins-ci.org/browse/JENKINS-44252) - Make auditLog optional.

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
