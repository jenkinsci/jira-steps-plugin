---
title: Release notes
tags: [getting_started]
keywords: release notes, announcements, what's new, new features
summary: "Change log."
sidebar: jira_sidebar
permalink: release_notes.html
---
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
