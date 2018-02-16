package org.thoughtslive.jenkins.plugins.jira.api

class InputBuilder {

    static appendDescription(object, description) {
        if (object.description) {
            return object.description += description
        }
    }

    static String getAttachmentLink(object) {
        return object.content
    }
}
