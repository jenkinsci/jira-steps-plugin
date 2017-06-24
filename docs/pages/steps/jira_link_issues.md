---
title: jiraLinkIssues
tags: [steps]
keywords: steps, link, issue
summary: "More about jiraLinkIssues step."
sidebar: jira_sidebar
permalink: jira_link_issues.html
folder: steps
---

## Overview

This step links two issues in the provided JIRA site.

Hint: Try getIssueLinkTypes to know the type.

## Input

* **type** - type of the link. (Ex: Relates, Blocks, Cloners, Duplicate)
* **inwardKey** - inward issue key.
* **outwardKey** - outward issue key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_link_issues step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2'
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2'
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    jiraLinkIssues type: 'Relates', inwardKey: 'TEST-1', outwardKey: 'TEST-2', site: 'LOCAL'
  ```

{% include links.html %}
