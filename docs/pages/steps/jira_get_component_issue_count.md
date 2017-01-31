---
title: jiraGetComponentIssueCount
tags: [steps]
keywords: steps, component
summary: "More about jiraGetComponentIssueCount step."
sidebar: jira_sidebar
permalink: jira_get_component_issue_count.html
folder: steps
---

## Overview

Get component issue count by id.

## Fields

* **id** - componentId.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def count = jiraGetComponentIssueCount id: 10024
      echo count.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def count = jiraGetComponentIssueCount id: 10024
        echo count.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def count = jiraGetComponentIssueCount site: "LOCAL", id: 10024, failOnError: false
    echo count.data.toString()
  ```
{% include links.html %}
