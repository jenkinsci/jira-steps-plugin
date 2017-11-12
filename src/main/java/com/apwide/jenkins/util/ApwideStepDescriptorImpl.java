package com.apwide.jenkins.util;

import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;

public abstract class ApwideStepDescriptorImpl extends JiraStepDescriptorImpl {

    @Override
    protected String getPrefix() {
	return "Apwide ";
    }

}
