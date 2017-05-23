---
title: Random Examples.
summary: "Few random examples and common usages."
tags: [getting_started]
sidebar: jira_sidebar
permalink: common_usages.html
folder: general
---

For instance, if you are releasing more than one project and you want to create a `release review jira issue`, add Description, Fix version, comment on the issue with error message for a failure in the release process or close the issue, if release is successful.

  * Script for the above scenario with error comments the JIRA:
      
    ```groovy
    import static java.util.UUID.randomUUID 

    node {
      stage('JIRA') {
        def issueKey;
        try {
          def issue = [fields: [ project: [key: 'TESTPRO'],
                                 summary: 'New JIRA Created from Jenkins.',
                                 description: 'New JIRA Created from Jenkins.',
                                 issuetype: [name: 'Task']]]
          
          // creates new issue
          def newIssue = jiraNewIssue issue: issue, site: 'LOCAL'

          issueKey = newIssue.data.key
          
          // creates new fix version tag
          def newVersion = jiraNewVersion version: [ name: "new-fix-version-"+ randomUUID() as String,
                                                     archived: false,
                                                     released: false,
                                                     description: 'desc',
                                                     project: 'TESTPRO'], site: 'LOCAL'
          
          def testIssue = [ fields: [ fixVersions: [newVersion.data] ] ]                                              
          
          // assigns the created fix version tag to the jira
          response = jiraEditIssue idOrKey: issueKey, issue: testIssue, site: 'LOCAL'
          
          // added an error manually for testing purposes
          error "Exception" 

          def transitionInput = [ transition: [ id: '21'] ]
          
          // jira is transitioned to closed state if there's no error
          jiraTransitionIssue idOrKey: issueKey, input: transitionInput, site: 'LOCAL'
          
          // adds a comment on the issue
          jiraAddComment idOrKey: issueKey, comment: "RELEASING JIRA SUCCESSFUL", site: 'LOCAL'
        } 
        catch(error) {
          // adds a comment on the issue when there's error
          jiraAddComment idOrKey: issueKey, comment: "ERROR RELEASING JIRA", site: 'LOCAL'
          
          // makes the build job fail
          currentBuild.result = 'FAILURE'
        }
      }
    }
    ```
  ![Failure](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_release_failure.png)

  * Script for the above scenario without error closes the JIRA:

    ```groovy
    import static java.util.UUID.randomUUID 

    node {
      stage('JIRA') {
        def issueKey;
        try {
          def issue = [fields: [ project: [key: 'TESTPRO'],
                                 summary: 'New JIRA Created from Jenkins.',
                                 description: 'New JIRA Created from Jenkins.',
                                 issuetype: [name: 'Task']]]
          
          // creates new issue
          def newIssue = jiraNewIssue issue: issue, site: 'LOCAL'

          issueKey = newIssue.data.key
          
          // creates new fix version tag
          def newVersion = jiraNewVersion version: [ name: "new-fix-version-"+ randomUUID() as String,
                                                     archived: false,
                                                     released: false,
                                                     description: 'desc',
                                                     project: 'TESTPRO'], site: 'LOCAL'
          
          def testIssue = [ fields: [ fixVersions: [newVersion.data] ] ]                                              
          
          // assigns the created fix version tag to the jira
          response = jiraEditIssue idOrKey: issueKey, issue: testIssue, site: 'LOCAL'

          def transitionInput = [ transition: [ id: '21'] ]
          
          // jira is transitioned to closed state if there's no error
          jiraTransitionIssue idOrKey: issueKey, input: transitionInput, site: 'LOCAL'
          
          // adds a comment on the issue
          jiraAddComment idOrKey: issueKey, comment: "RELEASING JIRA SUCCESSFUL", site: 'LOCAL'
        } 
        catch(error) {
          // adds a comment on the issue when there's error
          jiraAddComment idOrKey: issueKey, comment: "ERROR RELEASING JIRA", site: 'LOCAL'

          // makes the build job fail
          currentBuild.result = 'FAILURE'
        }
      }
    }
    ```
  ![Successful](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_release_successful.png)

## Add new fix version to existing issue.

* JQL Search.
* Create new version.
* Edit an issue.

``` groovy
node {
  stage('JIRA') {
    def searchResults = jiraJqlSearch jql: "project = TEST AND issuekey = 'TEST-1'"
    def issues = searchResults.data.issues
    for (i = 0; i <issues.size(); i++) {
      def fixVersion = jiraNewVersion version: [name: "new-fix-version-1.0",
                                                project: "TEST"]
      def testIssue = [fields: [fixVersions: [fixVersion.data]]]
      response = jiraEditIssue idOrKey: issues[i].key, issue: testIssue
    }
  }
}
```

## Append new fix version to existing issue.

* JQL Search.
* Create new version.
* Edit an issue.

```groovy
node {
  stage('JIRA') {
    def searchResults = jiraJqlSearch jql: "project = TEST AND issuekey = 'TEST-1'"
    def issues = searchResults.data.issues
    for (i = 0; i <issues.size(); i++) {
      def result = jiraGetIssue idOrKey: issues[i].key
      def newVersion = jiraNewVersion version: [name: "new-fix-version-1.1",
                                                project: "TEST"]
      def fixVersions = result.data.fields.fixVersions << newVersion.data
      def testIssue = [fields: [fixVersions: fixVersions]]
      response = jiraEditIssue idOrKey: issues[i].key, issue: testIssue
    }
  }
}
```
