# Initial Idea 
https://docs.atlassian.com/jira/REST/6.4.13/

* Component
  * getComponent
  * **newComponent**
  * **editComponent**
  * getComponentRelatedIssueCount
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

TODO
* Projects
  * getVersions
  * getComponents
  * getStatuses
* IssueLinks
  * getIssueLink
  * deleteIssueLink  
* Session - Need to think on how we can reuse session Id. (Or do we need it at all.?)
* Support Issue Custom Fields.
