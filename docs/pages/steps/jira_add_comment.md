---
title: jiraAddComment
tags: [steps]
keywords: steps, comment
summary: "More about jiraAddComment step."
sidebar: jira_sidebar
permalink: jira_add_comment.html
folder: steps
---

## Overview

This step adds a comment on a particular issue.

## Input

* **idOrKey** - Issue id or key.
* **comment** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append a panel to the comment with the build url and build user name.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_add_comment step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

![Comment](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_add_comment.png)

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraAddComment idOrKey: 'TEST-1', comment: 'test comment'
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraAddComment idOrKey: 'TEST-1', comment: 'test comment'
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraAddComment site: 'LOCAL', idOrKey: 'TEST-1', comment: 'test comment'
  ```

{% include links.html %}
