+++
title = "Configuration as Code Plugin"
description = "Adding sites through Configuration as Code plugin"
tags = ["Setup", "Get Started", "casc"]
weight = 4
date = "2019-06-05"
lastmodifierdisplayname = "daper"
+++

## Automate configuring via Configuration as Code Plugin

The following snippet will automatically configure the global settings of this plugin.

```yaml
unclassified:
  config:
    sites:
    - name: 'another'
      url: 'http://example.com'
      timeout: 10000
      readTimeout: 10000
      loginType: 'BASIC'
      userName: 'foo'
      password: 'some pass'
    - name: 'moar jira'
      url: 'http://example.com'
      timeout: 10000
      readTimeout: 10000
      loginType: 'OAUTH'
      consumerKey: 'my consumer key'
      privateKey: 'my private key'
      secret: 'super secret'
      token: 'my token'
```
