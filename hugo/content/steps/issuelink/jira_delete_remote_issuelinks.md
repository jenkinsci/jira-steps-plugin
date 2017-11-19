+++
title = "DeleteRemoteIssueLinks"
description = "More about jiraDeleteRemoteIssueLinks step."
tags = ["steps", "issue", "issuelink"]
weight = 9
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraDeleteRemoteIssueLinks

This step deletes all remote links of an issue.

#### Input

* **idOrKey** - issue id or key.
* **globalId** - global application id (Optional).
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_delete_remote_issue_links step can be reused later in your script by doing `response.data.required_field_name`.
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
        def issueLink = jiraDeleteRemoteIssueLinks idOrKey: 'TEST-27', globalId: '10000'
        echo issueLink.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def issueLink = jiraDeleteRemoteIssueLinks idOrKey: 'TEST-27', globalId: '10000'
          echo issueLink.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def issueLink = jiraDeleteRemoteIssueLinks idOrKey: 'TEST-27', globalId: '10000', site: 'LOCAL', failOnError: false
    echo issueLink.data.toString()
    ```
