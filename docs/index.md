---
title: JIRA Pipeline Steps
last_updated: Feb 12, 2016
tags: [getting_started]
summary: "Jenkins JIRA Pipeline Steps"
sidebar: jira_sidebar
permalink: index.html
---

## Overview

Jenkins Pipeline approach allows us to chain several stages together via scripts rather than manually creating multiple jobs and chaining them together. It simplifies the orchestration of the pipeline stages (build, test, and deployment) with automation especially with reusable steps.

But if we are looking to add a stage to integrate JIRA to Jenkins, this plugin is what we need to make your integration process reliable and faster.

For instance:

* Create a JIRA?, see the following, isn't so simple? :).

  ```groovy
  def issue = [fields: [ project: [key: 'TESTPRO'],
                         summary: 'New JIRA Created from Jenkins.',
                         description: 'New JIRA Created from Jenkins.',
                         issuetype: [name: 'Task']]]
  def newIssue = jiraNewIssue issue: issue, site: 'YOURJIRASITE'
  echo newIssue.data.key
  ```

* Comment on a JIRA?, say just to monitor the status of Jenkins job and here we go.

  ```groovy
  try {
      error "Exception"
  } catch(error) {
    def comment = "${BUILD_URL} FAILED - ${ERROR}"
    jiraAddComment idOrKey: 'GENERIC-999', comment: comment, site: 'YOURJIRASITE'
    currentBuild.result = 'FAILURE'
  }
  ```

* May be just close the JIRA? cause we just released a project and it is release review JIRA.

  ```groovy
  def transitionInput = [ transition: [ name: 'Close'] ]
  jiraTransitionIssue idOrKey: 'RELEASE-999', input: transitionInput, site: 'YOURJIRASITE'
  ```

* There are quite a few steps that this plugin supports currently, and will keep adding more.

Some of the common usage scenarios with multiple steps were available [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)

## Introduction

This jira-steps-plugin extends [pipeline DSL](https://jenkins.io/doc/book/pipeline/syntax/) by implementing more steps related to JIRA. It's created to enhance the usage of pipeline steps introduced in Jenkins 2.0, the main focus of this plugin is to make more generic steps around JIRA API, so that it makes orchestrator's job easier.

Get started by [installing](install) this plugin and also refer to the [Release Notes](release_notes) for more information.

**NOTE:** The documentation is always up to date with latest version (or master). If you are on old version please refer to the code.

{% include links.html %}
