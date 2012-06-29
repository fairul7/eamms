/*
 * TaxonomyMap.java
 *
 * Created on March 7, 2006, 3:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tms.cms.taxonomy.model;

import java.util.Date;

import com.tms.cms.core.model.ContentObject;
import kacang.model.DefaultDataObject;

/**
 *
 * @author oilai
 */
public class TaxonomyMap extends DefaultDataObject {
    
    private String taxonomyId;
    private String contentId;
    private String contentType;
    private String mapBy;
    private Date mapDate;
    private String className;
    
    private ContentObject contentObject;
    
    /** Creates a new instance of TaxonomyMap */
    public TaxonomyMap() {
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMapBy() {
        return mapBy;
    }

    public void setMapBy(String mapBy) {
        this.mapBy = mapBy;
    }

    public Date getMapDate() {
        return mapDate;
    }

    public void setMapDate(Date mapDate) {
        this.mapDate = mapDate;
    }

	public ContentObject getContentObject() {
		return contentObject;
	}

	public void setContentObject(ContentObject contentObject) {
		this.contentObject = contentObject;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
    
	
    
}
