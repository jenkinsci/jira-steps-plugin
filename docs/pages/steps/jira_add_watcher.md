---
title: jiraAddWatcher
tags: [steps]
keywords: steps, issue, watcher
summary: "More about jiraAddWatcher step."
sidebar: jira_sidebar
permalink: jira_add_watcher.html
folder: steps
---
## Overview

Add userName as watcher to the given issue.

## Fields

* **idOrKey** - Issue id or key.
* **userName** - userName.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraAddWatcher idOrKey: "TEST-1", userName: "Jenkins"
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraAddWatcher idOrKey: "TEST-1", userName: "Jenkins"
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraAddWatcher site: "LOCAL", idOrKey: "TEST-1", userName: "Jenkins"
  ```

{% include links.html %}
