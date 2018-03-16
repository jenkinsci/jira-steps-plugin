+++
title = "Jenkins Script"
description = "Adding Sites through Jenkins Script"
tags = ["Setup", "Get Started"]
weight = 3
date = "2017-11-12"
lastmodifierdisplayname = "Naresh Rayapati"
+++

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
    readTimeout: 10000,
    loginType: 'BASIC',
    userName: 'foo',
    password: 'some pass'
  ],
  [
    name: 'moar jira',
    url: 'http://example.com',
    timeout: 10000,
    readTimeout: 10000,
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
      site.setReadTimeout(s.optInt('readTimeout', 10000))
    } else { //loginType is OAUTH
      site.setConsumerKey(s.optString('consumerKey', ''))
      site.setPrivateKey(s.optString('privateKey', ''))
      site.setSecret(s.optString('secret', ''))
      site.setToken(s.optString('token', ''))
      site.setReadTimeout(s.optInt('readTimeout', 10000))
    }
    //setSites does not make sense as a name because you can only set one site instead of a list :-/
    config.setSites(site)
  }
}

//persist configuration to disk as XML
config.save()
```
