package org.thoughtslive.jenkins.plugins.jira.model;

import lombok.Getter;

public class Visibility {

    @Getter
    private final String type;
    @Getter
    private final String value;

    public Visibility(String value) {
        this.type = "role";
        this.value = value;
    }

}