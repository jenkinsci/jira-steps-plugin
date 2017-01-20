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
    
    
* IssueLinks
  * linkIssues
  * getIssueLink
  * deleteIssueLink
* IssueLinkTypes
* Project 
  * getProject
  * getVersions.
  * getComponents
  * getStatuses.
* Search
  * By JQL.
* Versions
  * getVersion
  * createVersion
  * updateVersion
  * deleteVersion
* Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
