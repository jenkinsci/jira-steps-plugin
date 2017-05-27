---
title: jiraGetProjectComponents
tags: [steps]
keywords: steps, project
summary: "More about jiraGetProjectComponents step."
sidebar: jira_sidebar
permalink: jira_get_project_components.html
folder: steps
---

## Overview

Query all components by project id or key.

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
      def project = jiraGetProjectComponents idOrKey: 'TEST'
      echo project.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def project = jiraGetProjectComponents idOrKey: 'TEST'
        echo project.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def project = jiraGetProjectComponents idOrKey: 'TEST', site: 'LOCAL'
    echo project.data.toString()
  ```

{% include links.html %}
