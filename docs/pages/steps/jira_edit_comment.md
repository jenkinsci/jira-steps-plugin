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

Edit comment on an issue by given comment id.

## Fields

* **idOrKey** - Issue id or key.
* **commentId** - comment id.
* **comment** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraEditComment idOrKey: "TEST-1", commentId: "1000", comment: "test comment"
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraEditComment idOrKey: "TEST-1", commentId: "1000", comment: "test comment"
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraEditComment site: "LOCAL", idOrKey: "TEST-1", commentId: "1000", comment: "test comment"
  ```

{% include links.html %}
