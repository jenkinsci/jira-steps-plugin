---
title: jiraGetProjectStatuses
tags: [steps]
keywords: steps, statuses
summary: "More about jiraGetProjectStatuses step."
sidebar: jira_sidebar
permalink: jira_get_project_statuses.html
folder: steps
---

## Overview

Query all statuses by project id or key.

## Input

* **idOrKey** - project id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def statuses = jiraGetProjectStatuses idOrKey: 'TEST'
      echo statuses.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def statuses = jiraGetProjectStatuses idOrKey: 'TEST'
        echo statuses.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def statuses = jiraGetProjectStatuses idOrKey: 'TEST', site: 'LOCAL'
    echo statuses.data.toString()
  ```

{% include links.html %}
