---
title: jiraGetVersion
tags: [steps]
keywords: steps, version
summary: "More about jiraGetVersion step."
sidebar: jira_sidebar
permalink: jira_get_version.html
folder: steps
---

## Overview

This step queries the project version from the provided JIRA site.

## Input

* **id** - version id.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

Note: For more information about input, please refer to the model objects in the [api](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_version step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def version = jiraGetVersion id: '10000'
      echo version.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def version = jiraGetVersion id: '10000'
        echo version.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def version = jiraGetVersion id: '10000', site: 'LOCAL', failOnError: false
    echo version.data.toString()
  ```

{% include links.html %}
