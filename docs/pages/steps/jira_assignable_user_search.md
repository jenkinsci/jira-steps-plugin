---
title: jiraAssignableUserSearch
tags: [steps]
keywords: steps, user, assignable,  search, issue
summary: "More about AssignableUserSearchStep step."
sidebar: jira_sidebar
permalink: jira_assignable_user_search.html
folder: steps
---

## Overview

Search users by name, username or email address those can be assignable to given project and/or issueKey.

## Input

* **queryStr** - name, username or email address. (partial string are allowed)
* **project** - project key. Either project or issueKey is mandatory.
* **issueKey** - issue key. Either project or issueKey is mandatory.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def users = jiraAssignableUserSearch project: 'TEST'
      echo users.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def users = jiraAssignableUserSearch project: 'TEST', issueKey: 'TEST-01'
        echo users.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def users = jiraAssignableUserSearch  queryStr: 'jenkin', project: 'TEST', site: 'LOCAL', failOnError: true
    echo users.data.toString()
  ```

{% include links.html %}
