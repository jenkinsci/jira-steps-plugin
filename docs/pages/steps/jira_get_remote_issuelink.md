---
title: jiraGetRemoteIssueLink
tags: [steps]
keywords: steps, remote, issuelink
summary: "More about jiraGetRemoteIssueLink step."
sidebar: jira_sidebar
permalink: jira_get_remote_issuelink.html
folder: steps
---

## Overview

Query issue remote link by link id.

## Input

* **idOrKey** - issue id or key.
* **linkId** - remote link id.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000'
      echo issueLink.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000'
        echo issueLink.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000', site: 'LOCAL', failOnError: false
    echo issueLink.data.toString()
  ```

{% include links.html %}
