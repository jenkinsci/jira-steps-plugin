---
title: Contributing
tags: [getting_started]
keywords: questions, troubleshooting, contact, support
summary: "Guidelines for Contributing."
sidebar: jira_sidebar
permalink: contributing.html
folder: general
---

# Contributing

Help us to make this project better by contributing. Whether it's new features, bug fixes, or simply improving documentation, your contributions are welcome. Please start with logging a [JIRA][1] and submit a pull request.

Before you contribute, please review these guidelines to help ensure a smooth process for everyone. Thanks :).

## Issue reporting

* Please browse our existing issues before logging new issues.
* Check that the issue was not already fixed in the `master` branch.
* Open an issue with a descriptive title and a summary.
* Please be as clear and explicit as you can in your description of the issue.
* Include any relevant code in the issue summary.

## Pull requests

* Follow [Standard procedures in contributing to open source projects on Github][2].
  * Fork the project.
  * Use a feature branch.
  * Add [good commit messages][3].
  * Use the same coding conventions as the rest of the project. This project is using Google [StyleGuide](https://google.github.io/styleguide/javaguide.html).
  * Download StyleGuides from [Github](https://github.com/google/styleguide).
  * Commit locally and push to your fork until you are happy with your improvements.
  * Make sure to add tests and verify all the tests are passing when merging upstream.
  * Add an entry to the [Release notes][4] accordingly.
  * [Squash commits before merging to master][5].
  * Open a [pull request][6].
  * The pull request will be reviewed by the community and merged by the project committers.

## Development

* Fork the repository.
* Clone the forked repository.
* Make the code changes.
* Add Tests
* Open a pull request directly from the forked repository.

## Testing

* Spin up [docker](https://www.docker.com/) instances for JIRA and JENKINS locally or install JIRA and JENKINS on your machine.
* [Configure Jenkins](https://jenkinsci.github.io/jira-steps-plugin/config.html)
* Run **`mvn package`** from command line or terminal.
* jira-steps-plugin.hpi file can be found in target folder of the project.
* [Install plugin manually using .hpi file in jenkins](https://jenkins.io/doc/book/managing/plugins/#advanced-installation).
* Test your functionality.
* It helps reviewers if the screenshots of these evidences are added in the pull request.

**Note:** This plugin requires **`JDK 1.8`** and **`latest maven version`** to build.

## Updating Documentation

* This plugin currently using [github pages site](https://pages.github.com/) for hosting the project documentation.
* Documentation can be updated and a pull request can be issued by changing site pages in **`jira-steps-plugin/docs/pages/`** folder.
* Once the changes are committed on your branch, merge it to master branch of your fork and go to **`settings > github pages`** and select source as master branch/docs folder.
* Site will be published at https://YourUserName.github.io/jira-steps-plugin/
* More information about Jekyll [Setting up GitHub Pages site locally with Jekyll](https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll/).

[1]: http://issues.jenkins-ci.org/secure/IssueNavigator.jspa?mode=hide&reset=true&jqlQuery=project+%3D+JENKINS+AND+status+in+%28Open%2C+%22In+Progress%22%2C+Reopened%29+AND+component+%3D+%27jira-steps-plugin%27
[2]: http://gun.io/blog/how-to-github-fork-branch-and-pull-request
[3]: http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
[4]: ./release_notes.html
[5]: http://gitready.com/advanced/2009/02/10/squashing-commits-with-rebase.html
[6]: https://help.github.com/articles/using-pull-requests

{% include links.html %}
