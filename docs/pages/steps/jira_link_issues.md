---
title: jiraLinkIssues
tags: [steps]
keywords: steps, link, issue
summary: "More about jiraLinkIssues step."
sidebar: jira_sidebar
permalink: jira_link_issues.html
folder: steps
---

## Overview

Link two issues.

Hint: Try getIssueLinkTypes to know the type.

## Input

* **type** - type of the link. (Ex: Relates, Blocks, Cloners, Duplicate)
* **inwardKey** - inward issue key.
* **outwardKey** - outward issue key.
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
      jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2'
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2'
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2', site: 'LOCAL'
  ```

{% include links.html %}
