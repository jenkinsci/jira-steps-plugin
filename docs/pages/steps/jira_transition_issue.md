---
title: jiraTransitionIssue
tags: [steps]
keywords: steps, issue, transition
summary: "More about jiraTransitionIssue step."
sidebar: jira_sidebar
permalink: jira_transition_issue.html
folder: steps
---

## Overview

Transition issue to different state.

## Input

* **idOrKey** - Issue id or key.
* **input** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def transitionInput =
      [
          transition: [
              id: '5',
          ]
      ]

      jiraTransitionIssue idOrKey: 'TEST-1', input: transitionInput
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def transitionInput =
        [
            transition: [
                id: '5',
            ]
        ]

        jiraTransitionIssue idOrKey: 'TEST-1', input: transitionInput
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
  def transitionInput =
  [
      transition: [
          id: '5',
      ]
  ]

        jiraTransitionIssue idOrKey: 'TEST-1', input: transitionInput, site: 'LOCAL'
  ```

{% include links.html %}
