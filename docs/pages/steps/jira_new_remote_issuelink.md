---
title: jiraNewRemoteIssueLink
tags: [steps]
keywords: steps, remote, issuelink
summary: "More about jiraNewRemoteIssueLink step."
sidebar: jira_sidebar
permalink: jira_new_remote_issuelink.html
folder: steps
---

## Overview

Create new issue remote link.

## Input

* **idOrKey** - issue id or key.
* **remoteLink** - remote link.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Output

Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def remoteLink =  [globalId: "system=http://www.mycompany.com/support&id=1",
                         application: [type: "com.acme.tracker",
                                       name: "My Acme Tracker"],
                         relationship: "causes",
                         object: [url: "http://www.mycompany.com/support?id=1",
                                  title: "MYTEST-111"]]

      def issueLink = jiraNewRemoteIssueLink idOrKey: 'TEST-27', remoteLink: remoteLink
      echo issueLink.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def remoteLink =  [globalId: "system=http://www.mycompany.com/support&id=1",
                           application: [type: "com.acme.tracker",
                                         name: "My Acme Tracker"],
                           relationship: "causes",
                           object: [url: "http://www.mycompany.com/support?id=1",
                                    title: "MYTEST-111"]]

        def issueLink = jiraNewRemoteIssueLink idOrKey: 'TEST-27', remoteLink: remoteLink
        echo issueLink.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
  def remoteLink =  [globalId: "system=http://www.mycompany.com/support&id=1",
                     application: [type: "com.acme.tracker",
                                   name: "My Acme Tracker"],
                     relationship: "causes",
                     object: [url: "http://www.mycompany.com/support?id=1",
                              title: "MYTEST-111"]]

  def issueLink = jiraNewRemoteIssueLink idOrKey: 'TEST-27', remoteLink: remoteLink, site: 'LOCAL', failOnError: false
  echo issueLink.data.toString()
  ```

{% include links.html %}
