package com.tms.cms.core.model;

import kacang.services.indexing.Indexable;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultDataObject;

import java.util.Date;

import com.tms.cms.document.Document;
import com.tms.cms.image.Image;

public class ContentObjectIndexingWrapper extends DefaultDataObject implements Indexable {

    private static Log log = Log.getLog(ContentObjectIndexingWrapper.class);

    private String id;
    private String name;
    private String className;
    private String author;
    private String parentId;  
    private String fileName; 
    private Date date;
    private boolean archived;
    private boolean indexable;
    private boolean publishedIndex;

    public ContentObjectIndexingWrapper(ContentObject co, boolean publishedIndex) {
        this.id = co.getId();
        this.name = co.getName();
        this.indexable = co.isIndexable();
        this.publishedIndex = publishedIndex;
        this.className = co.getClassName();
        this.date = co.getDate();
        this.archived = co.isArchived();
        this.parentId = co.getParentId();
        this.fileName = "";
        this.author = (co.getAuthor() != null) ? co.getAuthor() : "";
        
        try {
        	if (Document.class.getName().equals(className) || Image.class.getName().equals(className)) {
        		Document doc;
	            Application app = Application.getInstance();
	            if (publishedIndex) {
	                ContentPublisher cp = (ContentPublisher)app.getModule(ContentPublisher.class);
	                doc = (Document)cp.view(id, null);
	            }
	            else {
	                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
	                doc = (Document)cm.view(id, null);
	            }
	            String file = doc.getFileName();
	            if(file != null && file.length() > 0) {
	            	file = doc.getFileName();
	            	int idx = file.lastIndexOf(".");
	            	if (idx >= 0) {
		            	file = file.substring(idx + 1, file.length());
	            	}
		            this.fileName = file;
            	}
        	}
        }
        catch (DataObjectNotFoundException e) {
            log.warn("Content " + id + " could not be indexed, not found");
        }
        catch (ContentException e) {
            log.error("Content " + id + " could not be indexed: " + e.toString());
        }

        
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndexableContent() {
        try {
            ContentObject co;
            Application app = Application.getInstance();
            if (publishedIndex) {
                ContentPublisher cp = (ContentPublisher)app.getModule(ContentPublisher.class);
                co = cp.view(id, null);
            }
            else {
                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                co = cm.view(id, null);
            }
            return co.getIndexableContent();
        }
        catch (DataObjectNotFoundException e) {
            log.warn("Content " + id + " could not be indexed, not found");
            return null;
        }
        catch (ContentException e) {
            log.error("Content " + id + " could not be indexed: " + e.toString());
            return null;
        }
    }

    public boolean isIndexable() {
        return indexable;
    }

    public String getClassName() {
        return className;
    }

    public Date getDate() {
        return date;
    }

    public boolean isArchived() {
        return archived;
    }

	public String getAuthor() {
		return author;
	}

	public String getParentId() {
		return parentId;
	}

	public String getFileName() {
		return fileName;
	}


}
