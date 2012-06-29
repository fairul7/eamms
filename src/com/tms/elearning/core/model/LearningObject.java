package com.tms.elearning.core.model;

import kacang.model.DefaultDataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * A ContentObject represents a generic unit of content. All content
 * types can be represented using ContentObjects,
 * e.g. Section, Article, Document, etc.
 */
public class LearningObject extends DefaultDataObject implements Serializable {

    //-- Identification
    private String id;
    private String name;
    private Collection children;
    /*private String aclId;
    private String parentId;
    private String ordering;
    private LearningObject parent;*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getChildren() {
        if (children == null)
            children = new ArrayList();
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public void addChild(LearningObject obj) {
        if (children == null)
            children = new ArrayList();
        children.add(obj);
    }

    public boolean containsChild(LearningObject obj) {
        if (children == null)
            return false;
        return children.contains(obj);
    }

    public void removeChild(LearningObject obj) {
        if (children != null)
            children.remove(obj);
    }

    public String toString() {
        return getId();
    }

  /*  public String getAclId() {
        return aclId;
    }

    public void setAclId(String aclId) {
        this.aclId = aclId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public LearningObject getParent() {
        return parent;
    }

    public void setParent(LearningObject parent) {
        this.parent = parent;
    }*/
}
