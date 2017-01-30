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

{% include links.html %}
