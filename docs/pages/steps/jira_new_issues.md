---
title: jiraNewIssues
tags: [steps]
keywords: steps, issue
summary: "More about jiraNewIssues step."
sidebar: jira_sidebar
permalink: jira_new_issues.html
folder: steps
---

## Overview

Creates issues in bulk.

## Fields

* **issues** - issues to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      # Look at IssueInput class for more information.
      def testIssue1 = [fields: [ project: [id: '10000'],
                           summary: "New JIRA Created from Jenkins.",
                           description: "New JIRA Created from Jenkins.",
                           issuetype: [id: '3']]]


      def testIssue2 = [fields: [ project: [id: '10000'],
                                  summary: "New JIRA Created from Jenkins.",
                                  description: "New JIRA Created from Jenkins.",
                                  issuetype: [id: '3']]]

      def testIssues = [issueUpdates: [testIssue1, testIssue2]]

      response = jiraNewIssues issues: testIssues

      echo response.successful.toString()
      echo response.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        # Look at IssueInput class for more information.
        def testIssue1 = [fields: [ project: [id: '10000'],
                             summary: "New JIRA Created from Jenkins.",
                             description: "New JIRA Created from Jenkins.",
                             issuetype: [id: 3]]]


        def testIssue2 = [fields: [ project: [id: '10000'],
                                    summary: "New JIRA Created from Jenkins.",
                                    description: "New JIRA Created from Jenkins.",
                                    issuetype: [id: '3']]]

        def testIssues = [issueUpdates: [testIssue1, testIssue2]]

        response = jiraNewIssues issues: testIssues

        echo response.successful.toString()
        echo response.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
  # Look at IssueInput class for more information.
  def testIssue1 = [fields: [ project: [id: '10000'],
                       summary: "New JIRA Created from Jenkins.",
                       issuetype: [id: '3']]]


  def testIssue2 = [fields: [ project: [id: '10000'],
                              summary: "New JIRA Created from Jenkins.",
                              description: "New JIRA Created from Jenkins.",
                              issuetype: [id: '3']]]

  def testIssues = [issueUpdates: [testIssue1, testIssue2]]

  response = jiraNewIssues issues: testIssues, site: "LOCAL"

  echo response.successful.toString()
  echo response.data.toString()
  ```

{% include links.html %}
