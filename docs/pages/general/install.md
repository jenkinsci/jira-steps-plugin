---
title: Installing JIRA Steps Plugin
tags: [getting_started]
summary: "Installing Plugin."
sidebar: jira_sidebar
permalink: install.html
folder: general
---
This plugin is currently under active development.

* **Install or upgrade released versions using the Jenkins UI.**

* Run `mvn package` to build a deployable hpi bundle for Jenkins.

* Build the code and copy the target/hubot-steps.hpi plugin bundle to $JENKINS_HOME/plugins/ directory, and restart Jenkins. Check pom.xml file for more dependencies.

Note this plugin **REQUIRES JDK 1.8** to build.

{% include links.html %}
