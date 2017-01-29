---
title: Release notes
tags: [getting_started]
keywords: release notes, announcements, what's new, new features
summary: "Change log."
sidebar: jira_sidebar
permalink: release_notes.html
---

* ## **1.0.0-SNAPSHOT**
  * Initial release. (Still under active development)
  * Target JIRA API Version [6.4.13](https://docs.atlassian.com/jira/REST/6.4.13/).
  * Steps to Support
    * Component
      * getComponent
      * newComponent
      * editComponent
      * getComponentIssueCount
    * Issue
      * getIssue
      * **newIssue**
      * **newIssues**
      * **editIssue**
      * assignIssue
    * Comments
      * getComments
      * addComment
      * editComment
      * getComment
    * Email
      * notify
    * Transitions
      * getTransitions
      * transitionIssue
    * Watchers
      * getWatchers
      * addWatcher
    * Project
      * getProjects
      * getProject
    * Versions
      * getVersion
      * createVersion
      * editVersion
    * IssueLinks
      * linkIssues
    * IssueLinkTypes
      * getIssueLinkTypes
    * Search
      * By JQL.

  * ## TODO
    * Projects
      * getVersions
      * getComponents
      * getStatuses
    * IssueLinks
      * getIssueLink
      * deleteIssueLink  
    * Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
    * Support Issue Custom Fields.


{% include links.html %}
