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

* **Basic Authentication**
    * Goto **Manage Jenkins > Configure System > JIRA Steps > Add Site > Choose Basic**.
      * Name = Jira Site Name
      * URL = Jira Site URL
      * User Name = Jira Account Username
      * Password = Jira Account Password

    ![Basic](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_site_basic.png)

* **OAuth Authentication**
    * Follow the [jira-rest-api-oauth-authentication guide](https://developer.atlassian.com/cloud/jira/platform/jira-rest-api-oauth-authentication/) to setup OAuth Authentication.
    * Add site to Jenkins.
      * Goto **Manage Jenkins > Configure System > JIRA Steps > Add Site > Choose OAuth**.
      * Name = Jira Site Name
      * URL = Jira Site URL
      * Consumer Key = consumer_key from `config.properties`.
      * Private Key = private_key from `config.properties`.
      * Secret = secret from `config.properties`.
      * Token  = access_token from `config.properties`.

   ![OAuth](https://raw.githubusercontent.com/ThoughtsLive/jira-steps/master/docs/images/jira_site_oauth.png)

## Optional Params for all Steps.

* `site` - Optional param which will override `JIRA_SITE` global variable.
* `failOnError` - is optional and by default it is `true`.

   **NOTE:** When set to true, then the step will abort the job as a failure when there is an error sending message. To make it false, it should always be provided as global environment variable `JIRA_FAIL_ON_ERROR`.

## Environment Variables.

* `JIRA_SITE` - Global variable to set default site for all JIRA steps.
* `JIRA_FAIL_ON_ERROR` - By default all steps `fail` the job when there is an error, by setting this to `false` all steps won't fail the job.

## Common Response & Error Handling.

Every step returns a common response, which will have more information about the request like `successful`, `error`, `data` and `code`. Always try catch if we want to handle abort exception or can set `failOrError` to `false` to ignore all the error.

* `successful` - Returns `true` or `false`. Status of the step.
* `code` - HTTP code, response code returned from JIRA. or `-1` if there is any internal server error.
* `data` - Corresponding object being returned from JIRA when request was successful. For instance `jiraGetProject` returns `Project` and `jiraGetVersion` returns `FixVersion`.
* `error` - Error message when the actual request to JIRA failed.

   Usually, if the error is
     * From JIRA, `code` will be `400`.
     * Caused by the plugin it will be `-1`.

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

## Automate configuring via Jenkins Script Console

The following [Jenkins Script Console script](https://wiki.jenkins.io/display/JENKINS/Jenkins+Script+Console) will automatically configure the global settings of this plugin.

```groovy
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import org.thoughtslive.jenkins.plugins.jira.Config
import org.thoughtslive.jenkins.plugins.jira.Site

//global user-defined configuration
JSONArray sites = [
  [
    name: 'another',
    url: 'http://example.com',
    timeout: 10000,
    loginType: 'BASIC',
    userName: 'foo',
    password: 'some pass'
  ],
  [
    name: 'moar jira',
    url: 'http://example.com',
    timeout: 10000,
    loginType: 'OAUTH',
    consumerKey: 'my consumer key',
    privateKey: 'my private key',
    secret: 'super secret',
    token: 'my token'
  ]
] as JSONArray

//get global Jenkins configuration
Config.ConfigDescriptorImpl config = Jenkins.instance.getExtensionList(Config.ConfigDescriptorImpl.class)[0]

//delete all existing sites
config.@sites.clear()

//configure new sites from the above JSONArray
sites.each { s ->
  String loginType = s.optString('loginType', '').toUpperCase()
  if(loginType in ['BASIC', 'OAUTH']) {
    Site site = new Site(s.optString('name',''), new URL(s.optString('url', '')), s.optString('loginType', ''), s.optInt('timeout', 10000))
    if(loginType == 'BASIC') {
      site.setUserName(s.optString('userName', ''))
      site.setPassword(s.optString('password', ''))
    } else { //loginType is OAUTH
      site.setConsumerKey(s.optString('consumerKey', ''))
      site.setPrivateKey(s.optString('privateKey', ''))
      site.setSecret(s.optString('secret', ''))
      site.setToken(s.optString('token', ''))
    }
    //setSites does not make sense as a name because you can only set one site instead of a list :-/
    config.setSites(site)
  }
}

//persist configuration to disk as XML
config.save()
```

{% include links.html %}
