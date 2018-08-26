+++
title = "WaitForIssueUpdate"
description = "More about jiraWaitForIssueUpdate step."
tags = ["steps", "webhook"]
weight = 1
date = "2018-08-19"
lastmodifierdisplayname = "Ludovic Cintrat"
+++

### jiraWaitForIssueUpdate

This step waits for an update on a particular issue.\
Jira instance shall be configured with a webhook to the Jenkins instance at `http://<jenkins>/jira-steps-webhook/notify`.
Only Jira "issue updated" events are handled for now.\
More information on [Jira webhooks](https://developer.atlassian.com/server/jira/platform/webhooks/).

#### Input

* **idOrKey** - Issue id or key.
* **field** - Issue field.
* **fieldValues** - Expected new field values.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
For more information about input, please refer to the model objects in the [API](https://github.com/jenkinsci/jira-steps-plugin/tree/master/src/main/java/org/thoughtslive/jenkins/plugins/jira/api) package.
{{% /notice %}}

#### Output

* New field value.

#### Examples

* Wait for an update of the 'status' field, either 'In Progress', either 'Done'.

    ```groovy
    node {
      stage('JIRA') {
        def newValue = jiraWaitForIssueUpdate site: 'Local', field: 'status', fieldValues: ['In Progress', 'Done'], idOrKey: 'TEST-1'
        echo newValue
      }
    }
    ```
