package org.thoughtslive.jenkins.plugins.jira.api

class InputBuilder {

    static appendDescription(object, description) {
        if (object.description) {
            return object.description += description
        }
    }
}
