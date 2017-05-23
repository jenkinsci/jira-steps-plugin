---
title: jiraEditVersion
tags: [steps]
keywords: steps, version
summary: "More about jiraEditVersion step."
sidebar: jira_sidebar
permalink: jira_edit_version.html
folder: steps
---

## Overview

Edit version based on given input, which should have some minimal information on that object.

Note: Sometimes it may not possible to directly edit version (rename it) without un tagging all of its current JIRAs.

TODO: probably we should try move version

## Input

* **version** - version to be edited.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append the build url and build user name to the description.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_edit_version step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def testVersion = [ id: '1000',
                          name: 'test-version',
                          archived: true,
                          released: true,
                          description: 'desc',
                          project: 'TEST' ]
      jiraEditVersion version: testVersion
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def testVersion = [ id: '1000',
                            name: 'test-version',
                            archived: true,
                            released: true,
                            description: 'desc',
                            project: 'TEST' ]
        jiraEditVersion version: testVersion
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def testVersion = [ id: '1000',
                        name: 'test-version',
                        archived: true,
                        released: true,
                        description: 'desc',
                        project: 'TEST' ]
    jiraEditVersion version: testVersion, site: 'LOCAL'
  ```

{% include links.html %}
