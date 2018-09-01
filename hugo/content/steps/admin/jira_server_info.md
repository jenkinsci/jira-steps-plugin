+++
title = "Server Info"
description = "More about jiraGetServerInfo step."
tags = ["steps", "server", "info"]
weight = 1
date = "2018-01-08"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetServerInfo

This step retrives the server info.

#### Input

* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

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
        def serverInfo = jiraGetServerInfo()
        echo serverInfo.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def serverInfo = jiraGetServerInfo()
          echo serverInfo.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def serverInfo = jiraGetServerInfo site: 'LOCAL', failOnError: true
    echo serverInfo.data.toString()
    ```
