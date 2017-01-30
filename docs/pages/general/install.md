---
title: Installing JIRA Steps Plugin
tags: [getting_started]
summary: "Installing Plugin."
sidebar: jira_sidebar
permalink: install.html
folder: general
---
This plugin is currently under active development.

* Run `mvn package` to build a deployable hpi bundle for Jenkins.

* Build the code and copy the target/hubot-steps.hpi plugin bundle to $JENKINS_HOME/plugins/ directory, and restart Jenkins. Check pom.xml file for more dependencies.

* This plugin is not yet hosted in the Jenkins Update Centre, so you cannot install or upgrade using the Jenkins UI.

Note this plugin **REQUIRES JDK 1.8** to build.

{% include links.html %}
