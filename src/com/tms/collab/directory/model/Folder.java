package com.tms.collab.directory.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class Folder extends DefaultDataObject {

    // id
    private String id;
    private String parentId;
    private Collection subfolders;

    // details
    private String name;
    private String description;

    // owner
    private String userId;

    // security
    private List memberIdList;

    // audit
    private Date auditDateCreated;
    private Date auditDateModified;
    private Date auditDateDeleted;
    private String auditUserCreated;
    private String auditUserModified;
    private String auditUserDeleted;

    // sync flags
    private boolean created;
    private boolean modified;
    private boolean deleted;
    private boolean archived;

    public Folder() {
        memberIdList = new ArrayList();
        subfolders = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List getMemberIdList() {
        return memberIdList;
    }

    public void setMemberIdList(List memberIdList) {
        this.memberIdList = memberIdList;
    }

    public Date getAuditDateCreated() {
        return auditDateCreated;
    }

    public void setAuditDateCreated(Date auditDateCreated) {
        this.auditDateCreated = auditDateCreated;
    }

    public Date getAuditDateModified() {
        return auditDateModified;
    }

    public void setAuditDateModified(Date auditDateModified) {
        this.auditDateModified = auditDateModified;
    }

    public Date getAuditDateDeleted() {
        return auditDateDeleted;
    }

    public void setAuditDateDeleted(Date auditDateDeleted) {
        this.auditDateDeleted = auditDateDeleted;
    }

    public String getAuditUserCreated() {
        return auditUserCreated;
    }

    public void setAuditUserCreated(String auditUserCreated) {
        this.auditUserCreated = auditUserCreated;
    }

    public String getAuditUserModified() {
        return auditUserModified;
    }

    public void setAuditUserModified(String auditUserModified) {
        this.auditUserModified = auditUserModified;
    }

    public String getAuditUserDeleted() {
        return auditUserDeleted;
    }

    public void setAuditUserDeleted(String auditUserDeleted) {
        this.auditUserDeleted = auditUserDeleted;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Collection getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(Collection subfolders) {
        this.subfolders = subfolders;
    }

}
