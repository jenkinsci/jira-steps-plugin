+++
title = "Change Log"
description = "Change Log."
date = "2019-09-31"
lastmodifierdisplayname = "Naresh Rayapati"
+++

* #### **1.5.2** (Unreleased)


* #### **1.5.1**
  * [JENKINS-59280](https://issues.jenkins-ci.org/browse/JENKINS-59280) jiraJqlSearch Error Message: null value after upgrade.

* #### **1.5.0**
  * [JENKINS-57182](https://issues.jenkins-ci.org/browse/JENKINS-57182) When configuring a Jira steps site in casc yaml file. If the Jenkins service is restarted, the site is duplicated in Jenkins config.
      * <span style="color:red">Breaking changes: the java class *Config* has been renamed to *JiraStepsConfig*. Review the documentation to adapt your scripts.</span>
  * [JENKINS-58793](https://issues.jenkins-ci.org/browse/JENKINS-58793) Add "fields" parameter to api/2/search endpoint for jiraJqlSearch.

* #### **1.4.5**
  * [JENKINS-49394](https://issues.jenkins-ci.org/browse/JENKINS-49394) Make add/edit comment step more generic to set a visibility.
  * [JENKINS-56074](https://issues.jenkins-ci.org/browse/JENKINS-56074) Use 'null' as assignee to set an issue to unassigned.

* #### **1.4.4**
  * [JENKINS-53044](https://issues.jenkins-ci.org/browse/JENKINS-53044) Add step to retrieve jira server info.
      * Thank you [Stuart Rowe](https://github.com/stuartrowe) for the initial pull request.
* #### **1.4.3**
  * [JENKINS-50764](https://issues.jenkins-ci.org/browse/JENKINS-50764) Whitelist ResponseData method signatures in In-process script approval.
      * With 1.4.2 only getData was added to the whitelist but here added rest.
* #### **1.4.2**
  * [JENKINS-50764](https://issues.jenkins-ci.org/browse/JENKINS-50764) Whitelist ResponseData getData signature in In-process script approval.
* #### **1.4.1**
  * [JENKINS-50417](https://issues.jenkins-ci.org/browse/JENKINS-50417) download and upload attachments are not working on slaves.
* #### **1.4.0**
  * [JENKINS-45764](https://issues.jenkins-ci.org/browse/JENKINS-45764) Add issue attachment related steps.
      * jiraUploadAttachment
      * jiraGetAttachmentInfo
      * jiraDownloadAttachment
      * jiraDeleteAttachment
  * [JENKINS-50194](https://issues.jenkins-ci.org/browse/JENKINS-50194) Build userId missing for a job being built by other Jobs.
  * [JENKINS-49314](https://issues.jenkins-ci.org/browse/JENKINS-49314) Added readTimeout to Site configuration.
  * [JENKINS-50356](https://issues.jenkins-ci.org/browse/JENKINS-50356) Upgrade hugo theme to latest.

* #### **1.3.1**
  * [JENKINS-48104](https://issues.jenkins-ci.org/browse/JENKINS-48104) Fix Documentation Links.
  * [JENKINS-48097](https://issues.jenkins-ci.org/browse/JENKINS-48097) Delete deprecated jekyll documentation.
  * [JENKINS-48115](https://issues.jenkins-ci.org/browse/JENKINS-48115) Get rid of isMetaStep()==true everywhere.
* #### **1.3.0**
  * [JENKINS-47948](https://issues.jenkins-ci.org/browse/JENKINS-47948) Delete deprecated api classes with Object.
      * <span style="color:red">Note: EditVersion and EditComponent are updated to support all the JIRA Versions, which would break existing code. Please refer the appropriate steps guides for the new Syntax.</span>.
  * [JENKINS-47949](https://issues.jenkins-ci.org/browse/JENKINS-47949) Apply Google Style Guide.
  * [JENKINS-47954](https://issues.jenkins-ci.org/browse/JENKINS-47954) Migrate documentation to hugo.
  * <span style="color:red"> Please upgrade to 1.3.1, as I was reported some problems with declarative pipelines, see JENKINS-48115 for more details. </span>.
* #### **1.2.5**
  * [JENKINS-47914](https://issues.jenkins-ci.org/browse/JENKINS-47914) Use Jenkins user id instead of user name as buildUser.
* #### **1.2.4**
  * [JENKINS-47668](https://issues.jenkins-ci.org/browse/JENKINS-47668) jiraNewComponent/jiraEditComponent ends with an error.
      * <span style="color:red">Note: Component related steps (new/edit) would break as we have corrected a filed name (userType -> assigneeType)</span>.
* #### **1.2.3**
  * [JENKINS-44817](https://issues.jenkins-ci.org/browse/JENKINS-44817) NewIssueStep is not null safe when there is no description.
* #### **1.2.2**
  * [#42](https://github.com/jenkinsci/jira-steps-plugin/pull/40) Enhanced NewIssues step to support custom fields.
* #### **1.2.1**
  * [#40](https://github.com/jenkinsci/jira-steps-plugin/pull/40) Relaxed few step's input validation.
* #### **1.2.0**
  * [JENKINS-44254](https://issues.jenkins-ci.org/browse/JENKINS-44254) Ability to retrieve and update custom fields.
      * All Steps: No restrictions on what(fields) can be sent to JIRA and retrieve from JIRA. Every field can either be int or string.
  * [JENKINS-44460](https://issues.jenkins-ci.org/browse/JENKINS-44460) No DataBoundConstructor on joda.time.DateTime.
  * [JENKINS-44400](https://issues.jenkins-ci.org/browse/JENKINS-44400) Add more steps related to Projects.
      * jiraGetProjectVersions.
      * jiraGetProjectComponents.
      * jiraGetProjectStatuses.
  * [JENKINS-44253](https://issues.jenkins-ci.org/browse/JENKINS-44253) Add few user related steps.
      * jiraUserSearch.
      * jiraAssignableUserSearch.
  * [JENKINS-44399](https://issues.jenkins-ci.org/browse/JENKINS-44399) Add steps related to IssueLinks.
      * jiraGetIssueLink.
      * jiraDeleteIssueLink.
      * jiraGetRemoteIssueLink.
      * jiraGetRemoteIssueLinks.
      * jiraNewRemoteIssueLink.
      * jiraDeleteRemoteIssueLink.
      * jiraDeleteRemoteIssueLinks.
* #### **1.1.0**
  * <span style="color:red">Upgrading to this version will break few existing steps, please read the following notes for more information.</span>.
  * Multiple Fixes: [#29](https://github.com/jenkinsci/jira-steps-plugin/issues/29) Make `id` type consistent across and other minor fixes.
    * Made `id` type consistent (to `String`) across all the objects. (Non Passive change, existing code need to be changed to use Strings, this is applicable for mostly all the steps).
    * Changes to `jiraNewIssue` and `jiraNewIssues`.
      * Allow issue type look up by name (`fields->issuetype->name`).
      * Allow project look up by key (`fields->project->key`).
    * `@ToString` is duplicate in all api objects (`@Data` should take care of it already.)
    * `Array` type can be just either `List` or `Set`. Changed across all objects. No need of as String[] anymore. Ex: `['a','b'] as String[]` is now just `['a', 'b']`. (Non Passive change).
    * `fields` variable is in the wrong position for `Transitions`.
    * Updated documentation accordingly.
  * [#28](https://github.com/jenkinsci/jira-steps-plugin/issues/28) ISSUE-28 Enable more fields while creating/updating issues.
  * Added Terms and Conditions - Google Analytics.
  * [JENKINS-44252](https://issues.jenkins-ci.org/browse/JENKINS-44252) - Make auditLog optional.
* #### **1.0.3**
  * Fix: [#15](https://github.com/jenkinsci/jira-steps-plugin/issues/15) - Serialization error while querying component using getComponent.
  * Enhancement: [#17](https://github.com/jenkinsci/jira-steps-plugin/issues/17) - Expose access to the parent node for issue.
* #### **1.0.2**
  * Documentation update. No functional change.
* #### **1.0.1**
  * Fix: [#3](https://github.com/jenkinsci/jira-steps-plugin/issues/3) - Error editing issue with existing fix version.
  * More documentation.
* #### **1.0.0**
  * Initial release.
  * Manual tested with JIRA API Version [6.4.13](https://docs.atlassian.com/jira/REST/6.4.13/).
  * Steps to Support
    * Component
      * jiraGetComponent
      * jiraNewComponent
      * jiraEditComponent
      * jiraGetComponentIssueCount
    * Issue
      * jiraGetIssue
      * jiraNewIssue
      * jiraNewIssues
      * jiraEditIssue
      * jiraAssignIssue
    * Comments
      * jiraGetComments
      * jiraAddComment
      * jiraEditComment
      * jiraGetComment
    * Email
      * jiraNotifyIssue
    * Transitions
      * jiraGetTransitions
      * jiraTransitionIssue
    * Watchers
      * jiraGetWatches
      * jiraAddWatcher
    * Project
      * jiraGetProjects
      * jiraGetProject
    * Versions
      * jiraGetVersion
      * jiraNewVersion
      * jiraEditVersion
    * IssueLinks
      * jiraLinkIssues
    * IssueLinkTypes
      * jiraGetIssueLinkTypes
    * Search
      * jiraJqlSearch
