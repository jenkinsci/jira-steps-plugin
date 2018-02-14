+++
title = "GetAttachmentInfo"
description = "More about jiraGetAttachmentInfo step."
tags = ["steps", "attachment", "issue"]
weight = 2
date = "2018-02-14"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetAttachmentInfo

This step queries a particular attachment's info on an issue.

#### Input

* **id** - Attachment Id.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
For more information about input, please refer to the model objects in the [API](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here]({{%relref "getting-started/examples"%}})
* All the available fields for a JIRA response can be found in [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

{{% notice note %}}
`response.data` returns all the fields returned by JIRA API.
{{% /notice %}}

#### Examples

* With default [site]({{%relref "getting-started/config/common.md#global-environment-variables"%}}) from global variables.

    ```groovy
    node {
      stage('JIRA') {
        def attachment = jiraGetAttachmentInfo id: '1000'
        echo attachment.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def attachment = jiraGetAttachmentInfo id: '1000'
          echo attachment.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def attachment = jiraGetAttachmentInfo id: '1000', site: 'LOCAL'
    echo attachment.data.toString()
    ```
