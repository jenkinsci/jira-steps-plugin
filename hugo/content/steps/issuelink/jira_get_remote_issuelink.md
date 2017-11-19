+++
title = "GetRemoteIssueLink"
description = "More about jiraGetRemoteIssueLink step."
tags = ["steps", "issue", "issuelink"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetRemoteIssueLink

This step queries a particular remote link of an issue.

#### Input

* **idOrKey** - issue id or key.
* **linkId** - remote link id.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_get_remote_issue_link step can be reused later in your script by doing `response.data.required_field_name`.
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
        def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000'
        echo issueLink.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000'
          echo issueLink.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def issueLink = jiraGetRemoteIssueLink idOrKey: 'TEST-27', linkId: '10000', site: 'LOCAL', failOnError: false
    echo issueLink.data.toString()
    ```
