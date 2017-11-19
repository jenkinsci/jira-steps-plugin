+++
title = "NewVersion"
description = "More about jiraNewVersion step."
tags = ["steps", "version"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraNewVersion

Creates new version based on given input, which should have some minimal information on that object.

#### Input

* **version** - version to be created.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.
* **auditLog** - Optional. default: `true`. Append the build url and build user name to the description.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_new_version step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here]({{%relref "getting-started/examples"%}})
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

{{% notice note %}}
`response.data` returns all the fields returned by JIRA API.
{{% /notice %}}

#### Examples

* With default [site]({{%relref "getting-started/config/common.md#global-environment-variables"%}}) from global variables.

    ```groovy
    node {
      stage('JIRA') {
        def testVersion = [ name: 'test-version',
                            archived: true,
                            released: true,
                            description: 'desc',
                            project: 'TEST' ]
        jiraNewVersion version: testVersion
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def testVersion = [ name: 'test-version',
                              archived: true,
                              released: true,
                              description: 'desc',
                              project: 'TEST' ]
          jiraNewVersion version: testVersion
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def testVersion = [ name: 'test-version',
                        archived: true,
                        released: true,
                        description: 'desc',
                        project: 'TEST' ]
    jiraNewVersion version: testVersion, site: 'LOCAL'
    ```
