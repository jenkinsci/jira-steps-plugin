---
title: jiraGetIssueLink
tags: [steps]
keywords: steps, issuelink
summary: "More about jiraGetIssueLink step."
sidebar: jira_sidebar
permalink: jira_get_issuelink.html
folder: steps
---

## Overview

This step queries issue links from the provided JIRA site.

## Input

* **id** - issue link's id.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_issuelink step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issueLink = jiraGetIssueLink id: '10000'
      echo issueLink.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issueLink = jiraGetIssueLink id: '10000'
        echo issueLink.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issueLink = jiraGetIssueLink id: '10000', site: 'LOCAL', failOnError: false
    echo issueLink.data.toString()
  ```

{% include links.html %}
