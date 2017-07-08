---
title: jiraGetProjectVersions
tags: [steps]
keywords: steps, versions
summary: "More about jiraGetProjectVersions step."
sidebar: jira_sidebar
permalink: jira_get_project_versions.html
folder: steps
---

## Overview

This step queries all the versions of a particular project.

## Input

* **idOrKey** - project id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_project_versions step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def versions = jiraGetProjectVersions idOrKey: 'TEST'
      echo versions.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def versions = jiraGetProjectVersions idOrKey: 'TEST'
        echo versions.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def versions = jiraGetProjectVersions idOrKey: 'TEST', site: 'LOCAL'
    echo versions.data.toString()
  ```

{% include links.html %}
