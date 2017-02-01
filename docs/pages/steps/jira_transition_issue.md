---
title: jiraTransitionIssue
tags: [steps]
keywords: steps, issue, transition
summary: "More about jiraTransitionIssue step."
sidebar: jira_sidebar
permalink: jira_transition_issue.html
folder: steps
---

## Overview

Transition issue to different state.

## Fields

* **idOrKey** - Issue id or key.
* **input** - comment, supports jira wiki formatting.
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

## Examples

* With default [site](config#environment-variables) from global variables.

  ```groovy
  node {
    stage('JIRA') {
      def transitionInput =
      [
          "update": [
              "comment": [
                  [
                      "add": [
                          "body": "Bug has been fixed."
                      ]
                  ]
              ]
          ],
          "transition": [
              "id": "5",
              "fields": [
                  "assignee": [
                      "name": "bob"
                  },
                  "resolution": [
                      "name": "Fixed"
                  ]
              ]
          ]
      ]

      jiraTransitionIssue idOrKey: "TEST-1", input: transitionInput
    }
  }
  ```
* `withEnv` to override the default site (or if there is not global site)

  ```groovy
  node {
    stage('JIRA') {
      withEnv(['JIRA_SITE=LOCAL']) {
        def transitionInput =
        [
            "update": [
                "comment": [
                    [
                        "add": [
                            "body": "Bug has been fixed."
                        ]
                    ]
                ]
            ],
            "transition": [
                "id": "5",
                "fields": [
                    "assignee": [
                        "name": "bob"
                    },
                    "resolution": [
                        "name": "Fixed"
                    ]
                ]
            ]
        ]

        jiraTransitionIssue idOrKey: "TEST-1", input: transitionInput
      }
    }
  }
  ```
* Without environment variables.

  ```groovy
        def transitionInput =
        [
            "update": [
                "comment": [
                    [
                        "add": [
                            "body": "Bug has been fixed."
                        ]
                    ]
                ]
            ],
            "transition": [
                "id": "5",
                "fields": [
                    "assignee": [
                        "name": "bob"
                    },
                    "resolution": [
                        "name": "Fixed"
                    ]
                ]
            ]
        ]

        jiraTransitionIssue idOrKey: "TEST-1", input: transitionInput, site: "LOCAL"
  ```

{% include links.html %}
