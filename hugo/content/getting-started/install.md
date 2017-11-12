+++
title = "Install"
description = "Installing JIRA Steps Plugin"
tags = ["Setup", "Get Started"]
weight = 1
date = "2017-10-21"
lastmodifierdisplayname = "Naresh Rayapati"
+++

Jenkins provides a couple of different methods for installing plugins

### Installing from UI.

* [Installing Plugin From Web UI](https://jenkins.io/doc/book/managing/plugins/#from-the-web-ui)
* [Updating Plugin](https://jenkins.io/doc/book/managing/plugins/#updating-a-plugin)

### Manual installation - For snapshots

* Clone the repository.
* Run `mvn package` to build a deployable hpi bundle for Jenkins.
* `jira-steps.hpi` file can be found in jira-steps-plugin/target folder.
* For more information [Advanced installation](https://jenkins.io/doc/book/managing/plugins/#advanced-installation)

{{% notice note %}}
  This plugin **`REQUIRES JDK 1.8`** to build.
{{% /notice %}}
