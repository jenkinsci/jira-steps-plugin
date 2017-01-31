---
title: jiraJqlSearch
tags: [steps]
keywords: steps, jql, search, issue
summary: "More about jiraJqlSearch step."
sidebar: jira_sidebar
permalink: jira_jql_search.html
folder: steps
---

## Overview

Search Issues by Jql.

## Fields

* **jql** - jql as a string.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issues = jiraJqlSearch id: "PROJECT = TEST"
      echo issues.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)
  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issues = jiraJqlSearch id: "PROJECT = TEST"
        echo issues.data.toString()
      }
    }
  }
  ```
* Without environment variables.
  ```groovy
    def issues = jiraJqlSearch id: "PROJECT = TEST", site: "LOCAL", failOnError: true
    echo issues.data.toString()
  ```

{% include links.html %}
