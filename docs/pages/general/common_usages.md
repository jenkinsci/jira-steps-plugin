---
title: Random Examples.
summary: "Few random examples and common usages."
tags: [getting_started]
sidebar: jira_sidebar
permalink: common_usages.html
folder: general
---

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
