package com.tms.cms.core.model;

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
public abstract class ContentObject extends DefaultDataObject implements Serializable, Comparable {

    //-- Identification
    private String id;
    private String className;

    //-- Content Data (Version Specific)
    private String name;
    private String description;
    private String summary;
    private String author;
    private Date date;
    private String keywords;
    private String related;
    private String contents;

    //-- Versioning
    private String version;

    //-- Status
    private boolean neww;
    private boolean modified;
    private boolean deleted;
    private boolean checkedOut;
    private boolean submitted;
    private boolean approved;
    private boolean published;
    private boolean archived;
    private Date checkOutDate;
    private String checkOutUser;
    private String checkOutUserId;
    private Date startDate;
    private Date endDate;
    private Date publishDate;
    private String publishVersion;
    private String publishUser;
    private String publishUserId;

    //-- Audit Details (Version Specific)
    private Date submissionDate;
    private String submissionUser;
    private String submissionUserId;
    private Date approvalDate;
    private String approvalUser;
    private String approvalUserId;
    private String comments;

    //-- Ordering & Relationships
    private String aclId;
    private String parentId;
    private String ordering;
    private ContentObject parent;
    private Collection children;

    //-- Misc
    private String template;
    
    //For Digest Module
//  added for kz digest
    private String country;
    private String sector;
    private String company;
    private String allsource;

    /**
     * Returns the Content Module class for this ContentObject.
     * @return
     */
    public abstract Class getContentModuleClass();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNew() {
        return neww;
    }

    public void setNew(boolean neww) {
        this.neww = neww;
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

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckOutUser() {
        return checkOutUser;
    }

    public void setCheckOutUser(String checkOutUser) {
        this.checkOutUser = checkOutUser;
    }

    public String getCheckOutUserId() {
        return checkOutUserId;
    }

    public void setCheckOutUserId(String checkOutUserId) {
        this.checkOutUserId = checkOutUserId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(String publishVersion) {
        this.publishVersion = publishVersion;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSubmissionUser() {
        return submissionUser;
    }

    public void setSubmissionUser(String submissionUser) {
        this.submissionUser = submissionUser;
    }

    public String getSubmissionUserId() {
        return submissionUserId;
    }

    public void setSubmissionUserId(String submissionUserId) {
        this.submissionUserId = submissionUserId;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(String approvalUser) {
        this.approvalUser = approvalUser;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAclId() {
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

    public ContentObject getParent() {
        return parent;
    }

    public void setParent(ContentObject parent) {
        this.parent = parent;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isContainer() {
        return getChildren().size() > 0;
    }

    public Collection getChildren() {
        if (children == null)
            children = new ArrayList();
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public void addChild(ContentObject obj) {
        if (children == null)
            children = new ArrayList();
        children.add(obj);
    }

    public boolean containsChild(ContentObject obj) {
        if (children == null)
            return false;
        return children.contains(obj);
    }

    public void removeChild(ContentObject obj) {
        if (children != null)
            children.remove(obj);
    }

    public String toString() {
        return getId();
    }

    public boolean equals(Object o) {
        if (o instanceof ContentObject) {
            ContentObject co = (ContentObject)o;
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
        if (obj instanceof ContentObject) {
            ContentObject co = (ContentObject)obj;
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
        if (getDescription() != null) {
            buffer.append("\n");
            buffer.append(getDescription());
        }
        if (getSummary() != null) {
            buffer.append("\n");
            buffer.append(getSummary());
        }
        return buffer.toString();
    }

	public String getAllsource() {
		return allsource;
	}

	public void setAllsource(String allsource) {
		this.allsource = allsource;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isNeww() {
		return neww;
	}

	public void setNeww(boolean neww) {
		this.neww = neww;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
}
