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

This step queries all the statuses of a particular project.

## Input

* **idOrKey** - project id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_project_statuses step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

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
