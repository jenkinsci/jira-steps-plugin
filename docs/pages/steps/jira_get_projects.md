---
title: jiraGetProjects
tags: [steps]
keywords: steps, projects
summary: "More about jiraGetProjects step."
sidebar: jira_sidebar
permalink: jira_get_projects.html
folder: steps
---

## Overview

Query all projects in the given site.

## Input

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
      def projects = jiraGetProjects()
      echo projects.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def projects = jiraGetProjects()
        echo projects.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def projects = jiraGetProjects(), site: 'LOCAL'
    echo projects.data.toString()
  ```

{% include links.html %}
