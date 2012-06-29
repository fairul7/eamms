package com.tms.collab.directory.model;

import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * Module handler to manage a simple company directory.
 * Extension of the AddressBookModule that introduces security by permission.
 */
public class CompanyModule extends AddressBookModule {

    public static final String MODULE_NAME = "CompanyModule";
    public static final String PUBLIC_USER_ID = "";

    public Contact addContact(Contact contact, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_COMPANIES, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to manage companies");
            }
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                contact.setApproved(false);
            }
            return super.addContact(contact, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error adding company", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public Contact updateContact(Contact contact, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!userId.equals(contact.getOwnerId()) && !security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_COMPANIES, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to update companies");
            }
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                contact.setApproved(false);
            }
            return super.updateContact(contact, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error updating company", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void moveContact(String contactId, String folderId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to move companies");
            }
            super.moveContact(contactId, folderId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error moving company", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public void deleteContact(String contactId, String userId) throws AddressBookException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            if (!security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                throw new AddressBookException("No permission to delete companies");
            }
            super.deleteContact(contactId, userId);
        }
        catch (SecurityException e) {
            throw new AddressBookException("Error deleting company", e);
        }
        catch (AddressBookException e) {
            throw e;
        }
    }

    public SearchResult search(String query, int start, int rows, String userId) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Collection contactList = getContactList(query, null, null, null, getSearchStatus(), "firstName", false, start, rows);
            for (Iterator i=contactList.iterator(); i.hasNext();) {
                Contact c = (Contact)i.next();
                SearchResultItem item = new SearchResultItem();
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, c.getDisplayName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, c.getPhone());
                item.setValueMap(valueMap);
                sr.add(item);
            }
            int count = getContactCount(query, null, null, null, getSearchStatus());
            sr.setTotalSize(count);
            return sr;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }
}
