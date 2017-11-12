+++
title = "EditComponent"
description = "More about jiraEditComponent step."
tags = ["steps", "component"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraEditComponent

Edit component based on given input, which should have some minimal information on that object.

{{% notice note %}}
Sometimes it may not possible to directly edit component (rename it) without un tagging all of its current JIRAs.
{{% /notice %}}

#### Input

* **id** - component id. (Note: Applicable only from version 1.3.0)
* **component** - component to be edited.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append the build url and build user name to the description.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_edit_component step can be reused later in your script by doing `response.data.required_field_name`.
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
        def testComponent = [ name: "test-component",
                              description: "desc",
                              project: "TEST" ]
        jiraEditComponent id: "1000", component: testComponent
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def testComponent = [ name: "test-component",
                                description: "desc",
                                project: "TEST" ]
          jiraEditComponent id: "1000", component: testComponent
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def testComponent = [ name: "test-component",
                          description: "desc",
                          project: "TEST" ]
    jiraEditComponent id: "1000", site: "LOCAL", component: testComponent
    ```
