---
title: jiraEditComponent
tags: [steps]
keywords: steps, component
summary: "More about jiraEditComponent step."
sidebar: jira_sidebar
permalink: jira_edit_component.html
folder: steps
---

## Overview

Edit component based on given input, which should have some minimal information on that object.

Note: Sometimes it may not possible to directly edit component (rename it) without un tagging all of its current JIRAs.

## Fields

* **component** - component to be edited.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def testComponent = [ id: 1000,
                            name: "test-component",
                            description: "desc",
                            project: "TEST" ]
      jiraEditComponent component: testComponent
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def testComponent = [ id: 1000,
                              name: "test-component",
                              description: "desc",
                              project: "TEST" ]
        jiraEditComponent component: testComponent
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def testComponent = [ id: 1000,
                          name: "test-component",
                          description: "desc",
                          project: "TEST" ]
    jiraEditComponent site: "LOCAL", component: testComponent
  ```

{% include links.html %}
