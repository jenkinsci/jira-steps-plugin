---
title: jiraUserSearch
tags: [steps]
keywords: steps, user, search, issue
summary: "More about jiraUserSearch step."
sidebar: jira_sidebar
permalink: jira_user_search.html
folder: steps
---

## Overview

This step searches users by name, username or email address form the JIRA SITE.

## Input

* **queryStr** - name, username or email address. (partial string are allowed)
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_user_search step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira component can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Example usages of generated script

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      // where jenkins is actual username
      def users = jiraUserSearch queryStr: 'jenk'
      echo users.data.toString()
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        // where Naresh Rayapati is actual name.
        def users = jiraUserSearch queryStr: 'Nare'
        echo users.data.toString()
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
    def users = jiraUserSearch queryStr: 'jenkins@thoughtslive.org', site: 'LOCAL', failOnError: true
    echo users.data.toString()
  ```

{% include links.html %}
