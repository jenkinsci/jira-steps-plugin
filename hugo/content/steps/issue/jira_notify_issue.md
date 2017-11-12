+++
title = "NotifyIssue"
description = "More about jiraNotifyIssue step."
tags = ["steps", "issue"]
weight = 8
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

### jiraNotifyIssue

This step notifies users (like watchers, assignee and so on..) of a particular issue.

#### Input

* **idOrKey** - Issue id or key.
* **notify** - more info about whom should we notify and so on.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

{{% notice note %}}
It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA API documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.
{{% /notice %}}

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* The api response of this jira_notify_issue step can be reused later in your script by doing `response.data.required_field_name`.
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
        def notify = [ subject: 'Update about TEST-01',
                       textBody: 'Just wanted to update about this issue...',
                       htmlBody: 'Just wanted to update about this issue...',
                       to: [ reporter: true,
                             assignee: true,
                             watchers: false,
                             voters: false,
                             users: [{
                                      name: 'rao'
                                    },
                                    {
                                      name: 'naresh'
                                    }]
                          ]
                      ]
        jiraNotifyIssue idOrKey: 'TEST-1', notify: notify
      }
    }
    ```
* `withEnv` to override the default site (or if there is not global site)

    ```groovy
    node {
      stage('JIRA') {
        withEnv(['JIRA_SITE=LOCAL']) {
          def notify = [ subject: 'Update about TEST-01',
                         textBody: 'Just wanted to update about this issue...',
                         htmlBody: 'Just wanted to update about this issue...',
                         to: [ reporter: true,
                               assignee: true,
                               watchers: false,
                               voters: false,
                               users: [{
                                        name: 'rao'
                                      },
                                      {
                                        name: 'naresh'
                                      }]
                            ]
                        ]
          jiraNotifyIssue idOrKey: 'TEST-1', notify: notify
        }
      }
    }
    ```
* Without environment variables.

    ```groovy
    def notify = [ subject: 'Update about TEST-01',
                   textBody: 'Just wanted to update about this issue...',
                   htmlBody: 'Just wanted to update about this issue...',
                   to: [ reporter: true,
                         assignee: true,
                         watchers: false,
                         voters: false,
                         users: [{
                                  name: 'rao'
                                },
                                {
                                  name: 'naresh'
                                }]
                       ]
                  ]
    jiraNotifyIssue idOrKey: 'TEST-1', notify: notify, site: 'LOCAL'
    ```
