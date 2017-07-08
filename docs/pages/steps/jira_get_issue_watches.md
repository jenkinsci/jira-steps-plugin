---
title: jiraGetIssueWatches
tags: [steps]
keywords: steps, issue, watches
summary: "More about jiraGetIssueWatches step."
sidebar: jira_sidebar
permalink: jira_get_issue_watches.html
folder: steps
---

## Overview

This step queries all the issue watchers on a particular issue.

## Input

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_issue_watches step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def watches = jiraGetIssueWatches idOrKey: 'TEST-1'
      echo watches.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def watches = jiraGetIssueWatches idOrKey: 'TEST-1'
        echo watches.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def watches = jiraGetIssueWatches idOrKey: 'TEST-1', site: 'LOCAL'
    echo watches.data.toString()
  ```

{% include links.html %}
