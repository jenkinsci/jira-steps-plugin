---
title: jiraNotifyIssue
tags: [steps]
keywords: steps, issue, email, notify
summary: "More about jiraNotifyIssue step."
sidebar: jira_sidebar
permalink: jira_notify_issue.html
folder: steps
---

## Overview

This step notifies users (like watchers, assignee and so on..) of a particular issue.

## Input

* **idOrKey** - Issue id or key.
* **notify** - more info about whom should we notify and so on.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

**Note**: It supports all the fields that any jira instance supports including custom fields. For more information about all available input fields, please refer to the [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

## Output

* Each step generates generic output, please refer to this [link](config.html#common-response--error-handling) for more information.
* The api response of this jira_notify_issue step can be reused later in your script by doing `response.data.required_field_name`.
* You can see some example scenarios [here](https://jenkinsci.github.io/jira-steps-plugin/common_usages.html)
* All the available fields for a jira response can be found in [JIRA Api documentation](https://docs.atlassian.com/jira/REST/) depending on your JIRA version.

Note: response.data returns all the fields returned by JIRA API.

## Examples:

* With default [site](config#environment-variables) from global variables.

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

{% include links.html %}
