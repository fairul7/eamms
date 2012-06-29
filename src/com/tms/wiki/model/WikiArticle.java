package com.tms.wiki.model;

import kacang.model.DefaultDataObject;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 1:56:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class WikiArticle extends DefaultDataObject{
	

    //to create article
    private String title;
    private String id;
    private String articleId;    
    private String story;
    private String author;
    private Date createdOn;
    private String createdBy;
    private String categoryId;
    private String category;
    private String status;
    private String tags;
    private String displayTags;
    private String moduleName;
    private boolean articleStatus;
    private String module;
    private String moduleId;
    private String currentRevision;
    
    //edit page
    private String article;

    //add category
    private String newCategory;

    //for revision
    private String userName;

    private Number revision;
    private String revisionId;
    private String editSummary;
    private String revisedStory;
    private String modifiedBy;
    private Date modifiedOn;
    private String hostAddress;
    
    //leaveComments page    
    private String name;
    private String email;
    private String comments;
    private String commentId;
               

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }   
    
    public void setDisplayTags(String displayTags){
    	this.displayTags = displayTags;
    }
    // to split the tags and display with anchor tags.
    public String getDisplayTags(){
    	if(tags!=null){
    		return WikiUtil.splitTags(tags);
    	}
    	return "";
    }

    public Number getRevision() {
		return revision;
	}

	public void setRevision(Number revision) {
		this.revision = revision;
	}

	public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getEditSummary() {
        return editSummary;
    }

    public void setEditSummary(String editSummary) {
        this.editSummary = editSummary;
    }

    public String getRevisedStory() {
        return revisedStory;
    }

    public void setRevisedStory(String revisedStory) {
        this.revisedStory = revisedStory;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

   
    public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
    
    public String getStorySummary(){
    	String temp = "";
    	if (story != null)
    		temp = WikiConverter.convertToHtml(story);
    	
    	temp = WikiUtil.removeToc(temp);
    	if(temp.length()>WikiModule.STORY_SUMMARY_LENGTH)
    		return temp.substring(0,WikiModule.STORY_SUMMARY_LENGTH);
    	else
    		return temp;
    }
    
    public String getStoryToHtml(){
    	if (story != null)
    		return WikiConverter.convertToHtml(story);
    	else 
    		return "";
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isArticleStatus() {
		return articleStatus;
	}

	public void setArticleStatus(boolean articleStatus) {
		this.articleStatus = articleStatus;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(String currentRevision) {
		this.currentRevision = currentRevision;
	}
	
	
    
    
}
