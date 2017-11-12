+++
title = "Create New Fix Version"
description = "Tag new fix version to issue."
tags = ["Setup", "Get Started"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

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
