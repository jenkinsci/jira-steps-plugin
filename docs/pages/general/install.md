---
title: Installing JIRA Steps Plugin
tags: [getting_started]
summary: "Installing Plugin."
sidebar: jira_sidebar
permalink: install.html
folder: general
---

Jenkins provides a couple of different methods for installing plugins on the master

## Installing from UI.

* [Installing Plugin From Web UI](https://jenkins.io/doc/book/managing/plugins/#from-the-web-ui)
* [Updating Plugin](https://jenkins.io/doc/book/managing/plugins/#updating-a-plugin)

## Manual installation (May be for snapshots)

* Clone the repository.
* Run `mvn package` to build a deployable hpi bundle for Jenkins.
* `jira-steps.hpi` file can be found in jira-steps-plugin/target folder.
* For more information [Advanced installation](https://jenkins.io/doc/book/managing/plugins/#advanced-installation)

**Note:** This plugin **`REQUIRES JDK 1.8`** to build.

{% include links.html %}
