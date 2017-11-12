+++
title = "GetFields"
description = "More about jiraGetFields step."
tags = ["steps", "issue"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetFields

This step queries all fields from the provided JIRA site.

#### Input

* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_get_fields step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here]({{%relref "getting-started/examples"%}})
* All the available fields for a jira response can be found in [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

{{% notice note %}}
`response.data` returns all the fields including custom fields added in customizing your JIRA as an array.
{{% /notice %}}

#### Examples

* With default [site]({{%relref "getting-started/config/common.md#global-environment-variables"%}}) from global variables.

    ```groovy
    node {
      stage('JIRA') {
        def fields = jiraGetFields idOrKey: 'TEST-1'
        echo fields.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def fields = jiraGetFields idOrKey: 'TEST-1'
          echo fields.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
      def fields = jiraGetFields idOrKey: 'TEST-1', site: 'LOCAL'
      echo fields.data.toString()
    ```
