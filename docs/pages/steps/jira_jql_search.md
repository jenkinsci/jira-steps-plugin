---
title: jiraJqlSearch
tags: [steps]
keywords: steps, jql, search, issue
summary: "More about jiraJqlSearch step."
sidebar: jira_sidebar
permalink: jira_jql_search.html
folder: steps
---

## Overview

This step searches issues from the provided JIRA site by Jql.

## Input

* **jql** - jql as a string.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_jql_search step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API as an array.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def issues = jiraJqlSearch jql: 'PROJECT = TEST'
      echo issues.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def issues = jiraJqlSearch jql: 'PROJECT = TEST'
        echo issues.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def issues = jiraJqlSearch jql: 'PROJECT = TEST', site: 'LOCAL', failOnError: true
    echo issues.data.toString()
  ```

{% include links.html %}
