+++
title = "Append Fix Version"
description = "Append new Fix versions to existing issue."
tags = ["Setup", "Get Started"]
weight = 2
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

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
