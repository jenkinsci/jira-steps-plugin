+++
title = "GetProjectComponents"
description = "More about jiraGetProjectComponents step."
tags = ["steps", "project"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetProjectComponents

This step queries all components of a particular project.

#### Input

* **idOrKey** - project id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_get_project_components step can be reused later in your script by doing `response.data.required_field_name`.
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
        def components = jiraGetProjectComponents idOrKey: 'TEST'
        echo components.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def components = jiraGetProjectComponents idOrKey: 'TEST'
          echo components.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def components = jiraGetProjectComponents idOrKey: 'TEST', site: 'LOCAL'
    echo components.data.toString()
    ```
