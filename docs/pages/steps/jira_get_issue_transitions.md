---
title: jiraGetIssueTransitions
tags: [steps]
keywords: steps, transitions, issue
summary: "More about jiraGetIssueTransitions step."
sidebar: jira_sidebar
permalink: jira_get_issue_transitions.html
folder: steps
---

## Overview

Get all transitions of the given issue.

## Fields

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def transitions = jiraGetIssueTransitions idOrKey: "TEST-1"
      echo transitions.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def transitions = jiraGetIssueTransitions idOrKey: "TEST-1"
        echo transitions.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def transitions = jiraGetIssueTransitions idOrKey: "TEST-1"
    echo transitions.data.toString()
  ```


{% include links.html %}
