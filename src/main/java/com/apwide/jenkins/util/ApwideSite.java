package com.apwide.jenkins.util;

import org.thoughtslive.jenkins.plugins.jira.Site;

import com.apwide.jenkins.service.ApwideService;

public class ApwideSite {
    private Site site;
    private ApwideService apwideService;

    private ApwideSite(Site site) {
	this.site = site;
    }

    public static ApwideSite get(String siteName) {
	Site site = Site.get(siteName);
	// TODO reuse existing apwide site ?
	return new ApwideSite(site);
    }

    public ApwideService getApwideService() {
	if (apwideService == null) {
	    this.apwideService = new ApwideService(site);
	}
	return apwideService;
    }
}
