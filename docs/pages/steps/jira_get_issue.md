---
title: jiraGetIssue
tags: [steps]
keywords: steps, issue
summary: "More about jiraGetIssue step."
sidebar: jira_sidebar
permalink: jira_get_issue.html
folder: steps
---

## Overview

Get issue by id or key.

## Fields

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issue = jiraGetIssue idOrKey: "TEST-1"
      echo issue.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)
  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issue = jiraGetIssue idOrKey: "TEST-1"
        echo issue.data.toString()
      }
    }
  }
  ```
* Without environment variables.
  ```groovy
    def issue = jiraGetIssue idOrKey: "TEST-1", site: "LOCAL"
    echo issue.data.toString()
  ```

{% include links.html %}
