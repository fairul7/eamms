package com.tms.cms.digest.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class DigestContentDataObject extends DefaultDataObject {

    private String digestContentId;
    private String contentId;
    private String digestId;
    private String ordering;
    private Date dateCreate;
    private String name;
    private String summary;
    private String source;
    private Date sourceDate;
    private String country;
    private String sector;
    private String company;
    private String author;
    private String localSource;
    private String foreignSource;
    private String allsource;
    
    private String filename;	//for document only
    
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	public String getDigestContentId() {
		return digestContentId;
	}
	public void setDigestContentId(String digestContentId) {
		this.digestContentId = digestContentId;
	}
	public String getDigestId() {
		return digestId;
	}
	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrdering() {
		return ordering;
	}
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(Date sourceDate) {
		this.sourceDate = sourceDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAllsource() {
		String allsource = "";
        if (!(localSource == null||"-1".equals(localSource))) allsource += localSource + " ";
        if (!(foreignSource == null||"-1".equals(foreignSource))) allsource += foreignSource+ " ";
        if(!"".equals(allsource.trim()))
        	allsource+="by ";
        if (!(author == null||"-1".equals(author))) allsource += author;
		return allsource;
	}
	public void setAllsource(String allsource) {
		this.allsource = allsource;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getForeignSource() {
		return foreignSource;
	}
	public void setForeignSource(String foreignSource) {
		this.foreignSource = foreignSource;
	}
	public String getLocalSource() {
		return localSource;
	}
	public void setLocalSource(String localSource) {
		this.localSource = localSource;
	}  
}
