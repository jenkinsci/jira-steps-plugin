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

Help us to make this project better by contributing. Whether it's new features, bug fixes, or simply improving documentation, your contributions are welcome. Please start with logging a [JIRA][1] or submit a pull request.

Before you contribute, please review these guidelines to help ensure a smooth process for everyone.

Thanks.

## Issue reporting

* Please browse our [existing issues][1] before logging new issues.
* Check that the issue has not already been fixed in the `master` branch.
* Open an issue with a descriptive title and a summary.
* Please be as clear and explicit as you can in your description of the problem.
* Please state the version of Clojure and Clara you are using in the description.
* Include any relevant code in the issue summary.

## Pull requests

* Read [how to properly contribute to open source projects on Github][2].
* Fork the project.
* Use a feature branch.
* Write [good commit messages][3].
* Use the same coding conventions as the rest of the project. This project is using Google [StyleGuide](https://google.github.io/styleguide/javaguide.html).
  * Download StyleGuides from [Github](https://github.com/google/styleguide).
* Commit locally and push to your fork until you are happy with your contribution.
* Make sure to add tests and verify all the tests are passing when merging upstream.
* Add an entry to the [Changelog][4] accordingly.
* [Squash related commits together][6].
* Open a [pull request][7].
* The pull request will be reviewed by the community and merged by the project committers.

[1]: http://issues.jenkins-ci.org/secure/IssueNavigator.jspa?mode=hide&reset=true&jqlQuery=project+%3D+JENKINS+AND+status+in+%28Open%2C+%22In+Progress%22%2C+Reopened%29+AND+component+%3D+%27jira-steps-plugin%27
[2]: http://gun.io/blog/how-to-github-fork-branch-and-pull-request
[3]: http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
[4]: ./release_notes.html
[5]: ./LICENSE
[6]: http://gitready.com/advanced/2009/02/10/squashing-commits-with-rebase.html
[7]: https://help.github.com/articles/using-pull-requests
{% include links.html %}
