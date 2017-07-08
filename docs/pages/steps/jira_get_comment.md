---
title: jiraGetComment
tags: [steps]
keywords: steps, comment
summary: "More about jiraGetComment step."
sidebar: jira_sidebar
permalink: jira_get_comment.html
folder: steps
---

## Overview

This step queries a particular comment on an issue.

## Input

* **idOrKey** - Issue id or key.
* **commentId** - commentId.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_comment step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def comment = jiraGetComment idOrKey: 'TEST-1', commentId: '10004'
      echo comment.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def comment = jiraGetComment idOrKey: 'TEST-1', commentId: '10004'
        echo comment.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def comment = jiraGetComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '10004'
    echo comment.data.toString()
  ```

{% include links.html %}
