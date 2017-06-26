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

## Input

* **component** - component to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append the build url and build user name to the description.

**Note:** It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_get_new_component step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def testComponent = [ name: 'test-component',
                            description: 'desc',
                            project: 'TEST' ]
      jiraNewComponent component: testComponent
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def testComponent = [ name: 'test-component',
                              description: 'desc',
                              project: 'TEST' ]
        jiraNewComponent component: testComponent
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def testComponent = [ name: 'test-component',
                          description: 'desc',
                          project: 'TEST' ]
    jiraNewComponent component: testComponent, site: 'LOCAL'
  ```

{% include links.html %}
