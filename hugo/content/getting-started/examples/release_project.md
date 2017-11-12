+++
title = "Releasing a Project?"
description = "Few release related usages."
tags = ["Setup", "Get Started"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

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
