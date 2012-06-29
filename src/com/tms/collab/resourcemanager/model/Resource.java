package com.tms.collab.resourcemanager.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 24, 2003
 * Time: 2:35:16 PM
 * To change this template use Options | File Templates.
 */
public class Resource extends DefaultDataObject implements Comparable
{

    private String id;
    private String name;
    private String description;
    private String creator;
    private String modifiedBy;
    private String image;
    private String categoryId;
    private String category;
    private Date creationDate;
    private Date modificationDate;
    private int imageType=-1,classification;
    private String status;
    private boolean requireApproval=false;
    private boolean active=false;
    private boolean approved=false;
    private boolean deleted=false;
    private String bookingId;
    private Collection authorities;
    public Resource()
    {

    }

    public Resource(String id, String name, String description, String creator, String image, Date creationDate, Date modificationDate, boolean requireApproval, boolean active,boolean approved)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.image = image;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.requireApproval = requireApproval;
        this.active = active;
        this.approved = approved;
    }


    public Resource(String id, String name, String description, String creator, String image, boolean requireApproval,boolean approved)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.image = image;
        this.requireApproval = requireApproval;
        this.approved = approved;
    }

    public Resource(String id, String name, String description, String creator, String modifiedBy, String image, String categoryId, String category, Date creationDate, Date modificationDate, int imageType, int classification, boolean requireApproval, boolean active, boolean approved, boolean deleted)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.modifiedBy = modifiedBy;
        this.image = image;
        this.categoryId = categoryId;
        this.category = category;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.imageType = imageType;
        this.classification = classification;
        this.requireApproval = requireApproval;
        this.active = active;
        this.approved = approved;
        this.deleted = deleted;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }


    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }


    public boolean isRequireApproval()
    {
        return requireApproval;
    }

    public void setRequireApproval(boolean requireApproval)
    {
        this.requireApproval = requireApproval;
    }

    public int getImageType()
    {
        return imageType;
    }

    public void setImageType(int imageType)
    {
        this.imageType = imageType;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public int getClassification()
    {
        return classification;
    }

    public void setClassification(int classification)
    {
        this.classification = classification;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getBookingId()
    {
        return bookingId;
    }

    public void setBookingId(String bookingId)
    {
        this.bookingId = bookingId;
    }

    public Collection getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Collection authorities)
    {
        this.authorities = authorities;
    }

    public int compareTo(Object o)
    {
        if(o instanceof Resource){
            return getId().compareTo(((Resource)o).getId());
        }
        return -1;
    }
}
