---
title: jiraNewComponent
tags: [steps]
keywords: steps, component
summary: "More about jiraNewComponent step."
sidebar: jira_sidebar
permalink: jira_new_component.html
folder: steps
---

## Overview

Create new component based on given input, which should have some minimal information on that object.

![New Component](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_new_component.png)

## Fields

* **component** - component to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def testComponent = [ name: "test-component",
                            description: "desc",
                            project: "TEST" ]
      jiraNewComponent component: testComponent
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def testComponent = [ name: "test-component",
                              description: "desc",
                              project: "TEST" ]
        jiraNewComponent component: testComponent
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def testComponent = [ name: "test-component",
                          description: "desc",
                          project: "TEST" ]
    jiraNewComponent component: testComponent
  ```

{% include links.html %}
