---
title: jiraUserSearch
tags: [steps]
keywords: steps, user, search, issue
summary: "More about jiraUserSearch step."
sidebar: jira_sidebar
permalink: jira_user_search.html
folder: steps
---

## Overview

Search users by name, username or email address.

## Input

* **queryStr** - name, username or email address. (partial string are allowed)
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      // where jenkins is actual username
      def users = jiraUserSearch queryStr: 'jenk'
      echo users.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        // where Naresh Rayapati is actual name.
        def users = jiraUserSearch queryStr: 'Nare'
        echo users.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def users = jiraUserSearch queryStr: 'jenkins@thoughtslive.org', site: 'LOCAL', failOnError: true
    echo users.data.toString()
  ```

{% include links.html %}
