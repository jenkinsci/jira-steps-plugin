---
title: jiraGetIssue
tags: [steps]
keywords: steps, issue
summary: "More about jiraGetIssue step."
sidebar: jira_sidebar
permalink: jira_get_issue.html
folder: steps
---

## Overview

This step queries a particular issue from the provided JIRA site.

## Input

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_issue step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issue = jiraGetIssue idOrKey: 'TEST-1'
      echo issue.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issue = jiraGetIssue idOrKey: 'TEST-1'
        echo issue.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issue = jiraGetIssue idOrKey: 'TEST-1', site: 'LOCAL'
    echo issue.data.toString()
  ```

{% include links.html %}
