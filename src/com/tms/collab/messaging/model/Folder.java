package com.tms.collab.messaging.model;

import kacang.model.DefaultDataObject;

import java.util.Collection;

/**
 * Data object to represent a folder.
 */
public class Folder extends DefaultDataObject {
    private String folderId;
    private String userId;
    private String name;
    private String displayName;
    private boolean specialFolder;
    private long diskUsage;
    private String parentId;
    private Collection subfolders;

    public static final String FOLDER_INBOX = "Inbox";
    public static final String FOLDER_TRASH = "Trash";
    public static final String FOLDER_DRAFT = "Draft";
    public static final String FOLDER_SENT = "Sent";
    public static final String FOLDER_QM = "Quick Messages";
    public static final String FOLDER_OUTBOX = "Outbox";

    public String getId() {
        return getFolderId();
    }

    public void setId(String s) {
        setFolderId(s);
    }

    // === [ getters/setters ] =================================================
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * A special folder cannot be deleted. (INBOX, OUTBOX, DRAFT, SENT)
     * @return
     */
    public boolean isSpecialFolder() {
        return specialFolder;
    }

    /**
     * A special folder cannot be deleted. (INBOX, OUTBOX, DRAFT, SENT)
     * @param specialFolder
     */
    public void setSpecialFolder(boolean specialFolder) {
        this.specialFolder = specialFolder;
    }

    public long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Collection getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(Collection subfolders) {
        this.subfolders = subfolders;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
