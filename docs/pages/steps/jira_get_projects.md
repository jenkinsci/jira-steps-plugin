---
title: jiraGetProjects
tags: [steps]
keywords: steps, project
summary: "More about jiraGetProjects step."
sidebar: jira_sidebar
permalink: jira_get_projects.html
folder: steps
---

## Overview

Query all projects in the given site.

## Fields

* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def projects = jiraGetProjects()
      echo project.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def projects = jiraGetProjects()
        echo project.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def projects = jiraGetProjects()
    echo project.data.toString()
  ```

{% include links.html %}
