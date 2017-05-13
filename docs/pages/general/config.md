---
title: Configuring plugin
summary: "Configuration options"
tags: [getting_started]
sidebar: jira_sidebar
permalink: config.html
folder: general
---

## Authentication.

This plugin supports both Basic and OAuth, OAuth is preferred over the Basic authentication.

* **Basic**

  ![Basic](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_site_basic.png)
* **OAuth**
  1. Follow this [guide](https://developer.atlassian.com/cloud/jira/platform/jira-rest-api-oauth-authentication/) from JIRA to setup OAuth Authentication.
  2. Add site to Jenkins.
    * Goto Manage Jenkins -> Configure System -> JIRA Steps -> Add Site -> Choose OAuth.
    * Consumer Key = consumer_key from `config.properties`.
    * Private Key = private_key from `config.properties`.
    * Secret = secret from `config.properties`.
    * Token  = access_token from `config.properties`.

  ![OAuth](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_site_oauth.png)

## Optional Params for all Steps.

* `site` - Optional param which will override `JIRA_SITE` global variable.
* `failOnError` - is optional and by default it is `true`, if any error it won’t abort the job., it can also be provided as global environment variable `JIRA_FAIL_ON_ERROR`, environment variable takes the higher precedence.

## Environment Variables.

* `JIRA_SITE` - Global variable to set default site for all JIRA steps.
* `JIRA_FAIL_ON_ERROR` - By default all steps `fail` the job when there is an error, by setting this to `false` all steps won't fail the job.

## Common Response & Error Handling.

Every step returns a common response, which will have more information about the request like `successful`, `error`, `data` and `code`. Always try catch if we want to handle abort exception or can set `failOrError` to `false` to ignore all the error.

* `successful` - Returns `true` or `false`. Status of the step.
* `code` - HTTP code, response code returned from JIRA. or `-1` if there is any internal server error.
* `data` - Corresponding object being returned from JIRA when request was successful. (Example jiraGetProject returns Project, jiraGetVersion returns Version and so on.)
* `error` - Error message when the actual request to JIRA failed. Usually `code` would be `400` when there is error from JIRA or if that internal plugin error it would be `-1`.
Example:

```groovy
  def response = jiraGetComponent id: 10000
  echo response.successful
  echo response.code
  echo response.error
  echo response.data.toString()

  try {
    jiraGetComponent id: 10000
  } catch (error) {
    echo error
  }
```

{% include links.html %}
