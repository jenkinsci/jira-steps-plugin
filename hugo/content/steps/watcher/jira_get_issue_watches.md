+++
title = "GetIssueWatches"
description = "More about jiraGetIssueWatches step."
tags = ["steps", "watcher"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraGetIssueWatches

This step queries all the issue watchers on a particular issue.

#### Input

* **idOrKey** - Issue id or key.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
For more information about input, please refer to the model objects in the [API](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_get_issue_watches step can be reused later in your script by doing `response.data.required_field_name`.
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
        def watches = jiraGetIssueWatches idOrKey: 'TEST-1'
        echo watches.data.toString()
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def watches = jiraGetIssueWatches idOrKey: 'TEST-1'
          echo watches.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def watches = jiraGetIssueWatches idOrKey: 'TEST-1', site: 'LOCAL'
    echo watches.data.toString()
    ```
