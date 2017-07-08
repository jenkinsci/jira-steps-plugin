---
title: jiraGetFields
tags: [steps]
keywords: steps, fields
summary: "More about jiraGetFields step."
sidebar: jira_sidebar
permalink: jira_get_fields.html
folder: steps
---

## Overview

This step queries all fields from the provided JIRA site.

## Input

* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_fields step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields including custom fields added in customizing your JIRA  as an array.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def fields = jiraGetFields idOrKey: 'TEST-1'
      echo fields.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def fields = jiraGetFields idOrKey: 'TEST-1'
        echo fields.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def fields = jiraGetFields idOrKey: 'TEST-1', site: 'LOCAL'
    echo fields.data.toString()
  ```

{% include links.html %}
