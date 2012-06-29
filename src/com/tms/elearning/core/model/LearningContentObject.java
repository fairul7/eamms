package com.tms.elearning.core.model;

import kacang.model.DefaultDataObject;

import java.util.Collection;
import java.util.Date;
import java.io.Serializable;

public abstract class LearningContentObject extends DefaultDataObject implements Serializable, Comparable {
    // -- Identification
    private String id;
    private String className;

    // -- Content Data
    private String name;
    private String author;
    private String related;
    private String summary;
    private String content;



    private String categoryid;

    // -- Versioning
    private String version;

    // -- Content Status;
    private boolean neww;
    private boolean modified;
    private boolean deleted;
    private boolean active;
    private boolean publiz;
    private String publishVersion;

    // -- Relationship
    private Collection children;
    private LearningContentObject parent;
    private String parentId;

    // -- Sorting
    private String ordering;

    // -- Auditing
    private Date createdDate;
    private String createdByUser;
    private String createdByUserId;

    private Date auditDate;
    private String auditByUser;
    private String auditByUserId;

    /**
     * Returns the Content Module class for this ContentObject.
     * @return
     */
    public abstract Class getContentModuleClass();

    // -- Overidden Methods
    public String toString() {
        return getId();
    }

    public boolean equals(Object o) {
        if (o instanceof LearningContentObject) {
            LearningContentObject co = (LearningContentObject)o;
            try {
                return (getId().equals(co.getId()) && getVersion().equals(co.getVersion()));
            }
            catch (Exception e) {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Compares two objects. ContentObjects are compared according to the
     * ordering property.
     * @param obj
     * @return 0 if equal, 1 if greater, -1 if smaller
     */
    public int compareTo(Object obj) {
        if (obj instanceof LearningContentObject) {
            LearningContentObject co = (LearningContentObject)obj;
            if (ordering != null) {
                return ordering.compareTo(co.getOrdering());
            }
            else {
                return 1;
            }
        }
        else {
            return -1;
        }
    }

    /**
     * Returns the content that is to be indexed by the Indexing Service.
     * @return
     */
    public String getIndexableContent() {
        StringBuffer buffer = new StringBuffer(getName());
        if (getSummary() != null) {
            buffer.append("\n");
            buffer.append(getSummary());
        }
        return buffer.toString();
    }

    //-- Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPublic(boolean publiz) {
        this.publiz = publiz;
    }

    public boolean isPublic() {
        return publiz;
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

    public boolean isNew() {
        return neww;
    }

    public void setNew(boolean neww) {
        this.neww = neww;
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public LearningContentObject getParent() {
        return parent;
    }

    public void setParent(LearningContentObject parent) {
        this.parent = parent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

}
