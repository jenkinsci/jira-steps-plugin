---
title: jiraGetProject
tags: [steps]
keywords: steps, project
summary: "More about jiraGetProject step."
sidebar: jira_sidebar
permalink: jira_get_project.html
folder: steps
---

## Overview

Query project info by id or key.

## Fields

* **idOrKey** - project id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def project = jiraGetProject idOrKey: "TEST"
      echo project.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def project = jiraGetProject idOrKey: "TEST"
        echo project.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def project = jiraGetProject idOrKey: "TEST", site: "LOCAL"
    echo project.data.toString()
  ```

{% include links.html %}
