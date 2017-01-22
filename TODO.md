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
  * getProject
* Versions
  * getVersion
  * createVersion
  * updateVersion
* IssueLinks
  * linkIssues
* IssueLinkTypes
  * getIssueLinkTypes
* Search
  * By JQL.

TODO
* Projects
  * getVersions
  * getComponents
  * getStatuses
* IssueLinks
  * getIssueLink
  * deleteIssueLink  
* Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
