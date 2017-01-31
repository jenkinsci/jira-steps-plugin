---
title: jiraAssignIssue
tags: [steps]
keywords: steps, issue
summary: "More about jiraAssignIssue step."
sidebar: jira_sidebar
permalink: jira_assign_issue.html
folder: steps
---

## Overview

Assign issue to given user.

## Fields

* **idOrKey** - Issue id or key.
* **userName** - user.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraAssignIssue idOrKey: "TEST-1", userName: "Jenkins"
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)
  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraAssignIssue idOrKey: "TEST-1", userName: "Jenkins"
      }
    }
  }
  ```
* Without environment variables.
  ```groovy
    jiraAssignIssue site: "LOCAL", idOrKey: "TEST-1", userName: "Jenkins"
  ```

{% include links.html %}
