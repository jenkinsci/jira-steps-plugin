---
title: jiraEditComment
tags: [steps]
keywords: steps, comment
summary: "More about jiraEditComment step."
sidebar: jira_sidebar
permalink: jira_edit_comment.html
folder: steps
---
## Overview

This step updates a particular comment on particular issue.

## Input

* **idOrKey** - Issue id or key.
* **commentId** - comment id.
* **comment** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_edit_comment step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraEditComment idOrKey: 'TEST-1', commentId: '1000', comment: 'test comment'
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraEditComment idOrKey: 'TEST-1', commentId: '1000', comment: 'test comment'
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraEditComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '1000', comment: 'test comment'
  ```

{% include links.html %}
