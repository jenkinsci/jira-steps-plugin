+++
title = "EditIssue"
description = "More about jiraEditIssue step."
tags = ["steps", "issue"]
weight = 6
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraEditIssue

Updates an existing issue based on given input, which should have some minimal information on that object.

#### Input

* **idOrKey** - issue id or key.
* **issue** - issue to be updated.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_edit_issue step can be reused later in your script by doing `response.data.required_field_name`.
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
        # Look at IssueInput class for more information.
        def testIssue = [fields: [ // id or key must present for project.
                                   project: [id: '10000'],
                                   summary: 'New JIRA Created from Jenkins.',
                                   description: 'New JIRA Created from Jenkins.',
                                   customfield_1000: 'customValue',
                                   // id or name must present for issuetype.
                                   issuetype: [id: '3']]]

        response = jiraEditIssue idOrKey: 'TEST-01', issue: testIssue

        echo response.successful.toString()
        echo response.data.toString()
      }
    }
    ```

* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          # Look at IssueInput class for more information.
          def testIssue = [fields: [ project: [id: '10000'],
                                     summary: 'New JIRA Created from Jenkins.',
                                     description: 'New JIRA Created from Jenkins.',
                                     issuetype: [id: '3']]]

          response = jiraEditIssue idOrKey: 'TEST-01', issue: testIssue

          echo response.successful.toString()
          echo response.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    # Look at IssueInput class for more information.
    def testIssue = [fields: [ project: [id: '10000'],
                               summary: 'New JIRA Created from Jenkins.',
                               description: 'New JIRA Created from Jenkins.',
                               issuetype: [id: '3']]]

    response = jiraEditIssue idOrKey: 'TEST-01', issue: testIssue, site: 'LOCAL'

    echo response.successful.toString()
    echo response.data.toString()
    ```
