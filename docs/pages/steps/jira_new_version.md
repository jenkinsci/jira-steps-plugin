---
title: jiraNewVersion
tags: [steps]
keywords: steps, version
summary: "More about jiraNewVersion step."
sidebar: jira_sidebar
permalink: jira_new_version.html
folder: steps
---

## Overview

Creates new version based on given input, which should have some minimal information on that object.

## Fields

* **version** - version to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def testVersion = [ name: "test-version",
                          archived: true,
                          released: true,
                          description: "desc",
                          project: "TEST" ]
      jiraNewVersion version: testVersion
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def testVersion = [ name: "test-version",
                            archived: true,
                            released: true,
                            description: "desc",
                            project: "TEST" ]
        jiraNewVersion version: testVersion
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def testVersion = [ name: "test-version",
                        archived: true,
                        released: true,
                        description: "desc",
                        project: "TEST" ]
    jiraNewVersion version: testVersion, site: "LOCAL"
  ```

{% include links.html %}
