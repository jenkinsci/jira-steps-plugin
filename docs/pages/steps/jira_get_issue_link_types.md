---
title: jiraGetIssueLinkTypes
tags: [steps]
keywords: steps, IssueLinkTypes
summary: "More about jiraGetIssueLinkTypes step."
sidebar: jira_sidebar
permalink: jira_get_issue_link_types.html
folder: steps
---

## Overview

Get all issue links types.

## Fields

* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issueLinkTypes = jiraGetIssueLinkTypes()
      echo issueLinkTypes.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issueLinkTypes = jiraGetIssueLinkTypes()
        echo issueLinkTypes.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issueLinkTypes = jiraGetIssueLinkTypes site: "LOCAL", failOnError: false
    echo issueLinkTypes.data.toString()
  ```

{% include links.html %}
