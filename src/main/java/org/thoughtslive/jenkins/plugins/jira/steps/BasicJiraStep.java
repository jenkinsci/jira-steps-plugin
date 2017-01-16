package org.thoughtslive.jenkins.plugins.jira.steps;

import java.io.Serializable;

import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundSetter;

import lombok.Getter;

/**
 * Base class for all JIRA steps
 * 
 * @author Naresh Rayapati
 */
public abstract class BasicJiraStep extends AbstractStepImpl implements Serializable {

	private static final long serialVersionUID = 7268920801605705697L;

	@Getter
	@DataBoundSetter
	private String siteName;

	@Getter
	@DataBoundSetter
	private boolean failOnError = true;

}