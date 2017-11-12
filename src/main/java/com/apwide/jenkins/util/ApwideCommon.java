package com.apwide.jenkins.util;

import java.io.PrintStream;

import org.thoughtslive.jenkins.plugins.jira.util.Common;

public class ApwideCommon {

    public static void log(final PrintStream logger, final Object message) {
	Common.log(logger, message);
    }

}
