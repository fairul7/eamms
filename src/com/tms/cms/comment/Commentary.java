package com.tms.cms.comment;

/**
 * Container that contains Comments.
 */
public class Commentary extends Comment {

    public Commentary() {
    }

    public String[] getAllowedClassNames() {
        return new String[] { Comment.class.getName() };
    }

    public boolean isIndexable() {
        return false;
    }

}
