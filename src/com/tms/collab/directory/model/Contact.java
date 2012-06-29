package com.tms.collab.directory.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.Application;

import java.util.*;

public class Contact extends DefaultDataObject {

    public static final String PREFIX_CONTACT_GROUP = "[G]";

    // ids
    private String id;
    private String folderId;
    private String username;
    private String userId;
    private String companyId;

    // name
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickName;

    // core properties
    private String email;
    private String designation;
    private String company;
    private String address;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private String phone;
    private String extension;
    private String fax;
    private String mobile;
    private String comments;

    // custom
    private String customProperty;

    // owner
    private String ownerId;

    // status
    private boolean approved;

    // audit
    private Date auditDateCreated;
    private Date auditDateModified;
    private Date auditDateDeleted;
    private String auditUserCreated;
    private String auditUserModified;
    private String auditUserDeleted;

    // sync flags
    private boolean created;
    private boolean modified;
    private boolean deleted;
    private boolean archived;

    // contact group properties
    private boolean contactGroup;
    private String contactGroupIds;
    private String contactGroupIntranetIds; // user IDs for selected intranet users
    private String contactGroupEmails; // emails in contact group not in the address book

    //for sync
    private Date createdTime;
    private Date modifiedTime;

    public Contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getAuditDateCreated() {
        return auditDateCreated;
    }

    public void setAuditDateCreated(Date auditDateCreated) {
        this.auditDateCreated = auditDateCreated;
    }

    public Date getAuditDateModified() {
        return auditDateModified;
    }

    public void setAuditDateModified(Date auditDateModified) {
        this.auditDateModified = auditDateModified;
    }

    public Date getAuditDateDeleted() {
        return auditDateDeleted;
    }

    public void setAuditDateDeleted(Date auditDateDeleted) {
        this.auditDateDeleted = auditDateDeleted;
    }

    public String getAuditUserCreated() {
        return auditUserCreated;
    }

    public void setAuditUserCreated(String auditUserCreated) {
        this.auditUserCreated = auditUserCreated;
    }

    public String getAuditUserModified() {
        return auditUserModified;
    }

    public void setAuditUserModified(String auditUserModified) {
        this.auditUserModified = auditUserModified;
    }

    public String getAuditUserDeleted() {
        return auditUserDeleted;
    }

    public void setAuditUserDeleted(String auditUserDeleted) {
        this.auditUserDeleted = auditUserDeleted;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
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

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

// TODO: handle custom property

    public String getCustomProperty() {
        return customProperty;
    }

    public void setCustomProperty(String customProperty) {
        this.customProperty = customProperty;
    }

// TODO: handle display name

    public String getDisplayName() {
        StringBuffer name = new StringBuffer("");
        if (getLastName() != null && getLastName().trim().length() > 0) {
            name.append(getLastName());
            name.append(", ");
        }
        name.append(getFirstName());
        return name.toString();
    }

    /**
     * First name that is displayed in contact lists. Contact groups will appear differently
     * @return
     */
    public String getDisplayFirstName() {
        if (isContactGroup()) {
            return PREFIX_CONTACT_GROUP + firstName;
        }
        else {
            return firstName;
        }
    }

// Convenience methods to retrieve users from security service

    public User getAuditUserCreatedObject() {
        try {
            SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
            return sec.getUser(getAuditUserCreated());
        }
        catch (SecurityException e) {
            return null;
        }
    }

    public User getAuditUserModifiedObject() {
        try {
            SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
            return sec.getUser(getAuditUserModified());
        }
        catch (SecurityException e) {
            return null;
        }
    }

// Contact Group (distribution list) methods

    public boolean isContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(boolean contactGroup) {
        this.contactGroup = contactGroup;
    }

    public String getContactGroupIds() {
        return contactGroupIds;
    }

    public void setContactGroupIds(String contactGroupIds) {
        this.contactGroupIds = contactGroupIds;
    }

    public String getContactGroupIntranetIds() {
        return contactGroupIntranetIds;
    }

    public void setContactGroupIntranetIds(String contactGroupIntranetIds) {
        this.contactGroupIntranetIds = contactGroupIntranetIds;
    }

    public String getContactGroupEmails() {
        return contactGroupEmails;
    }

    public void setContactGroupEmails(String contactGroupEmails) {
        this.contactGroupEmails = contactGroupEmails;
    }

    public void setContactIdArray(String[] ids) {
        if (ids != null) {
            StringBuffer buffer = new StringBuffer();
            for(int i=0; i<ids.length; i++) {
                if (i > 0) {
                    buffer.append(',');
                }
                buffer.append(ids[i]);
            }
            setContactGroupIds(buffer.toString());
        }
        else {
            setContactGroupIds(null);
        }
    }

    public String[] getContactIdArray() {
        String ids = getContactGroupIds();
        if (ids != null) {
            Collection idList = new HashSet();
            StringTokenizer st = new StringTokenizer(ids, ",");
            while(st.hasMoreTokens()) {
                String tmp = st.nextToken().trim();
                if (tmp.length() > 0) {
                    idList.add(tmp);
                }
            }
            return (String[])idList.toArray(new String[0]);
        }
        else {
            return null;
        }
    }

    public void setContactList(Collection contactList) {
        if (contactList != null) {
            StringBuffer buffer = new StringBuffer();
            int c=0;
            for (Iterator i=contactList.iterator(); i.hasNext(); c++) {
                Contact contact = (Contact)i.next();
                if (c > 0) {
                    buffer.append(',');
                }
                buffer.append(contact.getId());
            }
            setContactGroupIds(buffer.toString());
        }
        else {
            setContactGroupIds(null);
        }
    }

    public void setIntranetIdArray(String[] ids) {
        if (ids != null) {
            StringBuffer buffer = new StringBuffer();
            for(int i=0; i<ids.length; i++) {
                if (i > 0) {
                    buffer.append(',');
                }
                buffer.append(ids[i]);
            }
            setContactGroupIntranetIds(buffer.toString());
        }
        else {
            setContactGroupIntranetIds(null);
        }
    }

    public String[] getIntranetIdArray() {
        String ids = getContactGroupIntranetIds();
        if (ids != null) {
            Collection idList = new HashSet();
            StringTokenizer st = new StringTokenizer(ids, ",");
            while(st.hasMoreTokens()) {
                String tmp = st.nextToken().trim();
                if (tmp.length() > 0) {
                    idList.add(tmp);
                }
            }
            return (String[])idList.toArray(new String[0]);
        }
        else {
            return null;
        }
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}

