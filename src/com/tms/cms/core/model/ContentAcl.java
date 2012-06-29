package com.tms.cms.core.model;

import java.io.Serializable;

public class ContentAcl implements Serializable, Comparable {

    private String role;
    private String principalId;
    private String objectId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ContentAcl) {
            ContentAcl acl = (ContentAcl)obj;
            return (role != null && role.equals(acl.getRole()) &&
                    principalId != null && principalId.equals(acl.getPrincipalId()) &&
                    objectId != null && objectId.equals(acl.getObjectId()));
        }
        else {
            return false;
        }
    }

    public int compareTo(Object obj) {
        return equals(obj) ? 0 : 1;
    }
}
