# Initial Idea 
https://docs.atlassian.com/jira/REST/6.4.13/

* Component
  * getComponent
  * newComponent
  * updateComponent
  * getComponentRelatedIssueCount
* Issue Related
  * Issue
    * getIssue
    * createIssue
    * createIssues
    * editIssue
    * assignIssue
  * Comments
    * getComments
    * addComment
    * updateComment
    * getComment
  * Email
    * notify
  * Transitions
    * getTransitions
    * doTransition
  * Watchers
    * getWatchers
    * addWatcher

* Project 
  * getProjects
  * getProject (Looks like this queries versions and components too)
  * getVersions
  * getComponents
  * getStatuses
* Versions
  * getVersion
  * createVersion
  * updateVersion
  * deleteVersion
* IssueLinks
  * linkIssues
  * getIssueLink
  * deleteIssueLink
* Search
  * By JQL.
* IssueLinkTypes
* Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
