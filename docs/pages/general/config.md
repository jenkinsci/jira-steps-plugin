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
  * Basic
  * OAuth

## Optional Params for all Steps.

* `site` - Optional param which will override `JIRA_SITE` global variable.
* `failOnError` - is optional and by default it is `true`, if any error it won’t abort the job., it can also be provided as global environment variable `JIRA_FAIL_ON_ERROR`, environment variable takes the higher precedence.

## Environment Variables.

* `JIRA_SITE` - Global variable to set default site for all JIRA steps.
* `JIRA_FAIL_ON_ERROR` - By default all steps `fail` the job when there is an error, by setting this to `false` all steps won't fail the job.

{% include links.html %}
