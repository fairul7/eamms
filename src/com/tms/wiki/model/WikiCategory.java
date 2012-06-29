package com.tms.wiki.model;

import kacang.model.DefaultDataObject;

import java.util.Collection;
import java.util.Date;

/**
 * Date: Dec 22, 2006
 * Time: 11:11:52 AM
 */
public class WikiCategory extends DefaultDataObject{

    private String categoryId;
    private String category;
    private Date createdOn;
    private String parentId;
    private String subCategory;
    private String subCategoryId;
    private String moduleId;
    private String moduleName;
    
    private Collection children;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }


    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

	public Collection getChildren() {
		return children;
	}

	public void setChildren(Collection children) {
		this.children = children;
	}
    
    
}
