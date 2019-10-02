+++
title = "JqlSearch"
description = "More about jiraJqlSearch step."
tags = ["steps", "jql", "search"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraJqlSearch

This step searches issues from the provided JIRA site by Jql.

#### Input

* **jql** - jql as a string.
* **fields** - Optional. the list of fields to return for each issue. default: null (in this case the parameter is 
not sent and all navigable fields are returned)
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_jql_search step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here]({{%relref "getting-started/examples"%}})
* All the available fields for a jira response can be found in [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

{{% notice note %}}
`response.data` returns all the fields returned by JIRA API.
{{% /notice %}}

#### Examples

* With default [site]({{%relref "getting-started/config/common.md#global-environment-variables"%}}) from global variables.

    ```groovy
    node {
      stage('JIRA') {
        def issues = jiraJqlSearch jql: 'PROJECT = TEST'
        echo issues.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def issues = jiraJqlSearch jql: 'PROJECT = TEST'
          echo issues.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def issues = jiraJqlSearch jql: 'PROJECT = TEST', site: 'LOCAL', failOnError: true
    echo issues.data.toString()
    ```
