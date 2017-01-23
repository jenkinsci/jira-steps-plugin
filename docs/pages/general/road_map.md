---
title: RoadMap
last_updated: Dec 25, 2016
tags: [getting_started]
summary: "Draft road map."
sidebar: jira_sidebar
permalink: road_map.html
folder: general
---
# Initial Idea 
[6.4.13](https://docs.atlassian.com/jira/REST/6.4.13/) (Support this version initially.)

* Component
  * getComponent
  * **newComponent**
  * **editComponent**
  * getComponentIssueCount
* Issue Related
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
    * **transitionIssue**
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

**TODO**
* Projects
  * getVersions
  * getComponents
  * getStatuses
* IssueLinks
  * getIssueLink
  * deleteIssueLink  
* Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
* Support Issue Custom Fields.
