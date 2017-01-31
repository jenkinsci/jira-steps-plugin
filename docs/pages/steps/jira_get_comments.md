---
title: jiraGetComments
tags: [steps]
keywords: steps, comment
summary: "More about jiraGetComments step."
sidebar: jira_sidebar
permalink: jira_get_comments.html
folder: steps
---

## Overview

Get all comments of the given issue.

## Fields

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def comments = jiraGetComments idOrKey: "TEST-1"
      echo comments.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def comments = jiraGetComments idOrKey: "TEST-1"
        echo comments.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def comments = jiraGetComments site: "LOCAL", idOrKey: "TEST-1"
    echo comments.data.toString()
  ```

{% include links.html %}
