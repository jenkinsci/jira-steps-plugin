---
title: Random Examples.
summary: "Few random examples and common usages."
tags: [getting_started]
sidebar: jira_sidebar
permalink: common_usages.html
folder: general
---

## Releasing a project?

For instance, if you are releasing more than one project and you want to create a `release review jira issue`, add Description, Fix version, comment on the issue with error message for a failure in the release process or close the issue, if release is successful.

* Create new Release review JIRA.
* Release your project.
* Create new fix version, just to tag a release review JIRA.
* Close issue if when releasing that project is successful.
* Finally if there is an error comment on a JIRA.

```groovy
import static java.util.UUID.randomUUID

node {
  stage('Releasing A Project') {
    def issueKey;
    try {
      def issue = [fields: [ project: [key: 'TESTPRO'],
                             summary: 'Release x.y.z Review',
                             description: 'Review changes for release x.y.z ',
                             issuetype: [name: 'Task']]]
      def newIssue = jiraNewIssue issue: issue
      issueKey = newIssue.data.key

      ...
      ...
      ...

      def newVersion = jiraNewVersion version: [ name: "new-fix-version-"+ randomUUID() as String,
                                                 description: 'desc',
                                                 project: 'TESTPRO'],

      def updateIssue = [ fields: [ fixVersions: [ newVersion.data]]]                                              
      def response = jiraEditIssue idOrKey: issueKey, issue: updateIssue

      def transitionInput = [transition: [name: 'Close']]
      jiraTransitionIssue idOrKey: issueKey, input: transitionInput
      jiraAddComment idOrKey: issueKey, comment: "RELEASING SUCCESSFUL"
    } catch(error) {
      jiraAddComment idOrKey: issueKey, comment: "${BUILD_URL} ERROR WHILE RELEASING ${error}"
      currentBuild.result = 'FAILURE'
    }
  }
}
```

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
