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

This step assigns a particular issue to a user.

## Input

* **idOrKey** - Issue id or key.
* **userName** - username of the person who should be added as a watcher.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_assign_issue step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraAssignIssue idOrKey: 'TEST-1', userName: 'Jenkins'
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraAssignIssue idOrKey: 'TEST-1', userName: 'Jenkins'
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraAssignIssue site: 'LOCAL', idOrKey: 'TEST-1', userName: 'Jenkins'
  ```

{% include links.html %}
