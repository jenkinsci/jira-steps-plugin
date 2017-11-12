+++
title = "GetComment"
description = "More about jiraGetComment step."
tags = ["steps", "comment", "issue"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetComment

This step queries a particular comment on an issue.

#### Input

* **idOrKey** - Issue id or key.
* **commentId** - commentId.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
For more information about input, please refer to the model objects in the [API](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_get_comment step can be reused later in your script by doing `response.data.required_field_name`.
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
        def comment = jiraGetComment idOrKey: 'TEST-1', commentId: '10004'
        echo comment.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def comment = jiraGetComment idOrKey: 'TEST-1', commentId: '10004'
          echo comment.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def comment = jiraGetComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '10004'
    echo comment.data.toString()
    ```
