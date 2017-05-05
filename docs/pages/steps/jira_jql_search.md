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

## Input

* **jql** - jql as a string.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issues = jiraJqlSearch jql: 'PROJECT = TEST'
      echo issues.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issues = jiraJqlSearch jql: 'PROJECT = TEST'
        echo issues.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issues = jiraJqlSearch jql: 'PROJECT = TEST', site: 'LOCAL', failOnError: true
    echo issues.data.toString()
  ```

{% include links.html %}
