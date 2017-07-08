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

This step creates new issues in bulk in the provided JIRA site.

## Input

* **issues** - issues to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append a panel to the comment with the build url and build user name.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_new_issues step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      # Look at IssueInput class for more information.
      def testIssue1 = [fields: [ project: [id: '10000'],
                                  summary: 'New JIRA Created from Jenkins.',
                                  description: 'New JIRA Created from Jenkins.',
                                  issuetype: [id: '3']]]


      def testIssue2 = [fields: [ project: [id: '10000'],
                                  summary: 'New JIRA Created from Jenkins.',
                                  description: 'New JIRA Created from Jenkins.',
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
                                    summary: 'New JIRA Created from Jenkins.',
                                    description: 'New JIRA Created from Jenkins.',
                                    issuetype: [id: '3']]]


        def testIssue2 = [fields: [ project: [id: '10000'],
                                    summary: 'New JIRA Created from Jenkins.',
                                    description: 'New JIRA Created from Jenkins.',
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
                              summary: 'New JIRA Created from Jenkins.',
                              issuetype: [id: '3']]]


  def testIssue2 = [fields: [ project: [id: '10000'],
                              summary: 'New JIRA Created from Jenkins.',
                              description: 'New JIRA Created from Jenkins.',
                              issuetype: [id: '3']]]

  def testIssues = [issueUpdates: [testIssue1, testIssue2]]

  response = jiraNewIssues issues: testIssues, site: 'LOCAL'

  echo response.successful.toString()
  echo response.data.toString()
  ```

{% include links.html %}
