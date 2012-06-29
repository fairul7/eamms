package com.tms.collab.directory.model;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;

/**
 * Module handler to manage a business directory.
 * Extension of the AddressBookModule that introduces security by permission.
 */
public class DirectoryModule extends AddressBookModule {

    public static final String MODULE_NAME = "DirectoryModule";
    public static final String PUBLIC_USER_ID = "";

    public static final String PERMISSION_MANAGE_FOLDERS = "com.tms.collab.directory.ManageFolders";
    public static final String PERMISSION_EDIT_COMPANIES = "com.tms.collab.directory.EditCompanies"; // add, edit
    public static final String PERMISSION_MANAGE_COMPANIES = "com.tms.collab.directory.ManageCompanies";  // for approval and deletion
    public static final String PERMISSION_EDIT_CONTACTS = "com.tms.collab.directory.EditContacts"; // add, edit
    public static final String PERMISSION_MANAGE_CONTACTS = "com.tms.collab.directory.ManageContacts"; // for approval and deletion

    public Folder addFolder(Folder folder, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_FOLDERS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to manage folders");
            }

            return super.addFolder(folder, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error adding folder", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void updateFolder(Folder folder, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_FOLDERS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to update folders");
            }
            super.updateFolder(folder, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error updating folder", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void moveFolder(String folderId, String parentId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_FOLDERS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to move folders");
            }
            super.moveFolder(folderId, parentId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error moving folder", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void deleteFolder(String folderId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_FOLDERS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to delete folders");
            }
            super.deleteFolder(folderId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error deleting folder", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public Contact addContact(Contact contact, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_CONTACTS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to add contacts");
            }
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                contact.setApproved(false);
            }
            return super.addContact(contact, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error adding contact", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public Contact updateContact(Contact contact, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!userId.equals(contact.getOwnerId()) && !security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_CONTACTS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to update contacts");
            }
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                contact.setApproved(false);
            }
            return super.updateContact(contact, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error updating contact", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void moveContact(String contactId, String folderId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to move contacts");
            }
            super.moveContact(contactId, folderId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error moving contact", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void deleteContact(String contactId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to delete contacts");
            }
            super.deleteContact(contactId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error deleting contact", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    protected String getSearchUserId(String userId) {
        return null;
    }

    protected Boolean getSearchStatus() {
        return Boolean.TRUE;
    }
}
