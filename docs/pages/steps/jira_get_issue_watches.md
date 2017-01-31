---
title: jiraGetIssueWatches
tags: [steps]
keywords: steps, issue, watches
summary: "More about jiraGetIssueWatches step."
sidebar: jira_sidebar
permalink: jira_get_issue_watches.html
folder: steps
---

## Overview

Get all issue watchers.

## Fields

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def watches = jiraGetIssueWatches idOrKey: "TEST-1"
      echo watches.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def watches = jiraGetIssueWatches idOrKey: "TEST-1"
        echo watches.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def watches = jiraGetIssueWatches idOrKey: "TEST-1", site: "LOCAL"
    echo watches.data.toString()
  ```

{% include links.html %}
