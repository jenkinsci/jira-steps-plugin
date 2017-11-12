+++
title = "NewIssues"
description = "More about jiraNewIssues step."
tags = ["steps", "issue"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraNewIssues

This step creates new issues in bulk in the provided JIRA site.

#### Input

* **issues** - issues to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append a panel to the comment with the build url and build user name.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_new_issues step can be reused later in your script by doing `response.data.required_field_name`.
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
        def testIssue1 = [fields: [ project: [id: '10000'],
                                    summary: 'New JIRA Created from Jenkins.',
                                    description: 'New JIRA Created from Jenkins.',
                                    issuetype: [id: '3']]]


        def testIssue2 = [fields: [ project: [id: '10000'],
                                    summary: 'New JIRA Created from Jenkins.',
                                    description: 'New JIRA Created from Jenkins.',
                                    issuetype: [id: '3']]]

        def testIssues = [issueUpdates: [testIssue1, testIssue2]]

        response = jiraNewIssues issues: testIssues

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
          def testIssue1 = [fields: [ project: [id: '10000'],
                                      summary: 'New JIRA Created from Jenkins.',
                                      description: 'New JIRA Created from Jenkins.',
                                      issuetype: [id: '3']]]


          def testIssue2 = [fields: [ project: [id: '10000'],
                                      summary: 'New JIRA Created from Jenkins.',
                                      description: 'New JIRA Created from Jenkins.',
                                      issuetype: [id: '3']]]

          def testIssues = [issueUpdates: [testIssue1, testIssue2]]

          response = jiraNewIssues issues: testIssues

          echo response.successful.toString()
          echo response.data.toString()
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    # Look at IssueInput class for more information.
    def testIssue1 = [fields: [ project: [id: '10000'],
                                summary: 'New JIRA Created from Jenkins.',
                                issuetype: [id: '3']]]


    def testIssue2 = [fields: [ project: [id: '10000'],
                                summary: 'New JIRA Created from Jenkins.',
                                description: 'New JIRA Created from Jenkins.',
                                issuetype: [id: '3']]]

    def testIssues = [issueUpdates: [testIssue1, testIssue2]]

    response = jiraNewIssues issues: testIssues, site: 'LOCAL'

    echo response.successful.toString()
    echo response.data.toString()
    ```
