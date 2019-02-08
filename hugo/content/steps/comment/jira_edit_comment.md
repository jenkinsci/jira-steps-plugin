+++
title = "EditComment"
description = "More about jiraEditComment step."
tags = ["steps", "comment", "issue"]
weight = 4
date = "2017-11-12"
lastmodifierdisplayname = "Benedikt Hr"
+++

### jiraEditComment

This step updates a particular comment on particular issue.

#### Input

* **idOrKey** - Issue id or key.
* **commentId** - comment id.
* **input** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* _Deprecated:_ **comment** - comment, supports jira wiki formatting.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_edit_comment step can be reused later in your script by doing `response.data.required_field_name`.
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
        def comment = [ body: 'test comment' ]
        jiraEditComment idOrKey: 'TEST-1', commentId: '1000', input: comment
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def comment = [ body: 'test comment' ]
          jiraEditComment idOrKey: 'TEST-1', commentId: '1000', input: comment
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
      def comment = [ body: 'test comment' ]
      jiraEditComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '1000', input: comment
    ```
* With limited visibility.

    ```groovy
      def comment = [ 
        body: 'test comment',
        visibility: [ 
          type: 'role', 
          value: 'Developer'
        ]
      ]
      jiraEditComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '1000', input: comment
    ```
* Deprecated.

    ```groovy
      jiraEditComment site: 'LOCAL', idOrKey: 'TEST-1', commentId: '1000', comment: 'test comment'
    ```
