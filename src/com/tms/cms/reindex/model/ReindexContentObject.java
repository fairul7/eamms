package com.tms.cms.reindex.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;
import kacang.stdui.CheckBox;

import com.tms.cms.core.model.ContentObject;

public class ReindexContentObject extends DefaultDataObject{
	
//	-- Identification
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
	
    private CheckBox chkBx;
    
    public Class getContentModuleClass(){
    	return null;
    }

	public CheckBox getChkBx() {
		return chkBx;
	}

	public void setChkBx(CheckBox chkBx) {
		this.chkBx = chkBx;
	}

	public String getAclId() {
		return aclId;
	}

	public void setAclId(String aclId) {
		this.aclId = aclId;
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

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
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

	public Collection getChildren() {
		return children;
	}

	public void setChildren(Collection children) {
		this.children = children;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNeww() {
		return neww;
	}

	public void setNeww(boolean neww) {
		this.neww = neww;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
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

	public String getPublishVersion() {
		return publishVersion;
	}

	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
	}

	public String getRelated() {
		return related;
	}

	public void setRelated(String related) {
		this.related = related;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
    
}
