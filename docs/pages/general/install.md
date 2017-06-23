---
title: Installing JIRA Steps Plugin
tags: [getting_started]
summary: "Installing Plugin."
sidebar: jira_sidebar
permalink: install.html
folder: general
---

### Jenkins provides a couple of different methods for installing plugins on the master

1. [Installing Plugin From Web UI](https://jenkins.io/doc/book/managing/plugins/#from-the-web-ui)

2. [Updating Plugin](https://jenkins.io/doc/book/managing/plugins/#updating-a-plugin)

3. [Advanced installation](https://jenkins.io/doc/book/managing/plugins/#advanced-installation)

### Packaging .hpi file for advanced installation

1. Clone the repository.

2. Run `mvn package` to build a deployable hpi bundle for Jenkins.

3. `jira-steps.hpi` file can be found in jira-steps-plugin/target folder.

**Note:** This plugin **`REQUIRES JDK 1.8`** to build.

{% include links.html %}