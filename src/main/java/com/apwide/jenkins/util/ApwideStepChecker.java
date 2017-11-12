package com.apwide.jenkins.util;

import hudson.Util;

public class ApwideStepChecker {

    public static void checkNotNull(String value, String paramName) {
	final String fixedValue = Util.fixEmpty(value);
	if (fixedValue == null) {
	    throw new RuntimeException("Param " + paramName + " cannot be null or empty!");
	}
    }
}
