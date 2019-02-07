package org.thoughtslive.jenkins.plugins.jira.model;

import lombok.Getter;

public class CommentParameter {

    @Getter
    private final String body;
    @Getter
    private final Visibility visibility;

    public CommentParameter(String body, String visibility) {
        this.body = body;
        if (visibility != null) {
            this.visibility = new Visibility(visibility);
        } else {
            this.visibility = null;
        }

    }

    public class Visibility {

        @Getter
        private final String type = "role";
        @Getter
        private final String value;

        public Visibility(String value) {
            this.value = value;
        }

    }

}
