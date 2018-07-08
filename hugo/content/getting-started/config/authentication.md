+++
title = "Authentication"
description = "Authentication through UI."
tags = ["Setup", "Get Started"]
weight = 1
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

## Authentication

This plugin supports both Basic and OAuth, OAuth is preferred over the Basic authentication.

### Basic Authentication

* Goto **Manage Jenkins > Configure System > JIRA Steps > Add Site > Choose Basic**.
  * Name = Jira Site Name
  * URL = Jira Site URL
  * User Name = Jira Account Username
  * Password = Jira Account Password

![Basic](https://raw.githubusercontent.com/jenkinsci/jira-steps-plugin/master/hugo/static/images/jira_site_basic.png)

{{% alert theme="info" %}} Please take note that above screenshot missing **ReadTimeout(ms)** {{% /alert %}}

### OAuth Authentication

* Follow the [jira-rest-api-oauth-authentication guide](https://developer.atlassian.com/cloud/jira/platform/jira-rest-api-oauth-authentication/) to setup OAuth Authentication.
* Add site to Jenkins.
  * Goto **Manage Jenkins > Configure System > JIRA Steps > Add Site > Choose OAuth**.
  * Name = Jira Site Name
  * URL = Jira Site URL
  * Consumer Key = consumer_key from `config.properties`.
  * Private Key = private_key from `config.properties`.
  * Secret = secret from `config.properties`.
  * Token  = access_token from `config.properties`.

![OAuth](https://raw.githubusercontent.com/jenkinsci/jira-steps-plugin/master/hugo/static/images/jira_site_oauth.png)

{{% alert theme="info" %}} Please take note that above screenshot missing **ReadTimeout(ms)** {{% /alert %}}
