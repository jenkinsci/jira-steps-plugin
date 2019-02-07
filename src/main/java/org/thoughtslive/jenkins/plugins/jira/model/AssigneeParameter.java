package org.thoughtslive.jenkins.plugins.jira.model;

import lombok.Getter;

public class AssigneeParameter {

    @Getter
    private final String name;

    public AssigneeParameter(String name) {
        this.name = name;
    }
    
}
