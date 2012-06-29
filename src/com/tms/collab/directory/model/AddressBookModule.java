package com.tms.collab.directory.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.services.indexing.SearchableModule;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResultItem;
import kacang.stdui.CountrySelectBox;
import kacang.Application;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.SequencedHashMap;
import com.tms.tmsPIMSync.VCardUtils;

/**
 * Generic address book module to handle contacts.
 * Represents a personal address book so no security restrictions are enforced.
 */
public class AddressBookModule extends DefaultModule implements SearchableModule {

    private Log log = Log.getLog(getClass());
    public static final String MODULE_NAME = "AddressBookModule";
    public static final String ROOT_FOLDER_ID = "";
    public static final String ROOT_FOLDER_NAME = "DirectoryRoot";

//--- folder mgmt methods

    public Folder getFolderTree(String userId) throws AddressBookException {

        try {
            Folder root = new Folder();
            root.setId("");
            root = getFolderListRecursively(root, userId);
            root.setId(ROOT_FOLDER_ID);
            root.setName(ROOT_FOLDER_NAME);
            return root;
        }
        catch (AddressBookException e) {
            throw new AddressBookException("Error getting folder tree for user " + userId, e);
        }
    }

    protected Folder getFolderListRecursively(Folder parent, String userId) throws AddressBookException {
        Collection children = getFolderList(null, parent.getId(), userId, "name", false, 0, -1);
        parent.setSubfolders(children);
        for (Iterator i=children.iterator(); i.hasNext();) {
            Folder subfolder = (Folder)i.next();
            if (subfolder.getId() != null && subfolder.getId().trim().length() > 0)
                getFolderListRecursively(subfolder, userId);
        }
        return parent;
    }

    public Folder getFolder(String folderId) throws DataObjectNotFoundException, AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectFolder(folderId);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting folder " + folderId, e);
        }
    }

    public Collection getFolderList(String query, String parentId, String userId, String sort, boolean desc, int start, int rows) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectFolderList(query, parentId, userId, sort, desc, start, rows);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting folder list", e);
        }
    }

    public int getFolderCount(String query, String parentId, String userId) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectFolderCount(query, parentId, userId);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting folder list", e);
        }
    }

    public Folder addFolder(Folder folder, String userId) throws AddressBookException {
        try {
            // generate id
            String uuid = UuidGenerator.getInstance().getUuid();
            folder.setId(uuid);

            // set user id
            folder.setUserId(userId);

            // set parent
            if (folder.getParentId() == null && !folder.getId().equals(folder.getParentId())) {
                folder.setParentId(ROOT_FOLDER_ID);
            }

            // store
            AddressBookDao dao = (AddressBookDao)getDao();
            dao.insertFolder(folder, userId);
            return folder;
        }
        catch (DaoException e) {
            throw new AddressBookException("Error adding folder", e);
        }
    }

    public void updateFolder(Folder folder, String userId) throws AddressBookException {
        try {
            // set user id
            folder.setUserId(userId);

            // prevent parent from being set to itself
            if (folder.getParentId() != null && folder.getId().equals(folder.getParentId())) {
                folder.setParentId(ROOT_FOLDER_ID);
            }

            AddressBookDao dao = (AddressBookDao)getDao();
            dao.updateFolder(folder, userId);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error updating folder", e);
        }
    }

    public void moveFolder(String folderId, String parentId, String userId) throws AddressBookException {
        try {
            Folder folder = getFolder(folderId);
            if (!folderId.equals(parentId) && !folderId.equals(folder.getParentId())) {
                folder.setParentId(parentId);
                updateFolder(folder, userId);
            }
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Error finding folder " + folderId + ": " + e.toString());
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error updating folder parent for " + folderId + ": " + e.toString());
        }
    }

    public void deleteFolder(String folderId, String userId) throws AddressBookException {
        try {
            // delete recursively
            Folder folder = getFolder(folderId);
            deleteFolderRecursively(folder, userId);
        }
        catch (DataObjectNotFoundException e) {
            throw new AddressBookException("Folder " + folderId + " not found for deletion", e);
        }
    }

    protected void deleteFolderRecursively(Folder folder, String userId) throws AddressBookException {
        try {
            Collection children = getFolderList(null, folder.getId(), userId, null, false, 0, -1);
            for (Iterator i=children.iterator(); i.hasNext();) {
                Folder subfolder = (Folder)i.next();
                // delete subfolders recursively
                deleteFolderRecursively(subfolder, userId);
            }

            // delete contacts
            Collection contactList = getContactList(null, folder.getId(), null, userId, null, null, false, 0, -1);
            for (Iterator j=contactList.iterator(); j.hasNext();) {
                Contact contact = (Contact)j.next();
                deleteContact(contact.getId(), userId);
            }

            // delete folder
            AddressBookDao dao = (AddressBookDao)getDao();
            dao.deleteFolder(folder.getId(), userId);

        }
        catch (AddressBookException e) {
            throw e;
        }
        catch (DaoException e) {
            throw new AddressBookException("Error deleting folder", e);
        }
    }

//-- Contact mgmt methods

    public Contact getContact(String contactId) throws AddressBookException, DataObjectNotFoundException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            Contact contact = dao.selectContact(contactId);

            // get country label
            String displayCountry = "";
            String country = contact.getCountry();
            if (country != null && !"-1".equals(country)) {
                CountrySelectBox csb = new CountrySelectBox();
                Map countryMap = csb.getOptionMap();
                displayCountry = (String)countryMap.get(country);
                if (displayCountry == null) {
                    displayCountry = country;
                }
            }
            contact.setProperty("displayCountry", displayCountry);
            return contact;
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact " + contactId, e);
        }
    }

    /**
     *
     * @param name
     * @param ownerId Set to null to retrieve regardless of owner
     * @return
     * @throws AddressBookException
     * @throws DataObjectNotFoundException
     */
    public Contact getContactGroupByName(String name, String ownerId, Boolean approved) throws AddressBookException, DataObjectNotFoundException {
        try {
            Contact contactGroup = null;
            AddressBookDao dao = (AddressBookDao)getDao();
            Collection candidates = dao.selectContactList(name, null, null, ownerId, approved, Boolean.TRUE, null, false, 0, -1);
            for (Iterator i=candidates.iterator(); i.hasNext();) {
                Contact c = (Contact)i.next();
                if (c.getFirstName().equals(name)) {
                    contactGroup = c;
                    break;
                }
            }
            if (contactGroup != null) {
                return contactGroup;
            }
            else {
                throw new DataObjectNotFoundException();
            }
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact group " + name, e);
        }
    }

    public Collection getContactList(String query, String folderId, String companyId, String ownerId, Boolean approved, String sort, boolean desc, int start, int rows) throws AddressBookException {
        return getContactList(query, folderId, companyId, ownerId, approved, null, sort, desc, start, rows);
    }

    public Collection getContactList(String query, String folderId, String companyId, String ownerId, Boolean approved, Boolean contactGroup, String sort, boolean desc, int start, int rows) throws AddressBookException {
        try {
            if ("displayFirstName".equals(sort)) {
                sort = "firstName";
            }
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectContactList(query, folderId, companyId, ownerId, approved, contactGroup, sort, desc, start, rows);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact list", e);
        }
    }

    public int getContactCount(String query, String folderId, String companyId, String ownerId, Boolean approved) throws AddressBookException {
        return getContactCount(query, folderId, companyId, ownerId, approved, null);
    }

    public int getContactCount(String query, String folderId, String companyId, String ownerId, Boolean approved, Boolean contactGroup) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectContactCount(query, folderId, companyId, ownerId, approved, contactGroup);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact list", e);
        }
    }

    public Collection getContactListByAlphabet(String query, String field, String folderId, String companyId, String ownerId, Boolean approved, String sort, boolean desc, int start, int rows) throws AddressBookException {
        try {
            if ("displayFirstName".equals(sort)) {
                sort = "firstName";
            }
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectContactListByAlphabet(query, field, folderId, companyId, ownerId, approved, sort, desc, start, rows);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact list", e);
        }
    }

    public int getContactCountByAlphabet(String query, String field, String folderId, String companyId, String ownerId, Boolean approved) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectContactCountByAlphabet(query, field, folderId, companyId, ownerId, approved);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact list", e);
        }
    }

    public Collection getContactListById(String[] contactIdArray, String sort, boolean desc) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            return dao.selectContactListById(contactIdArray, sort, desc);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error getting contact list by ID", e);
        }
    }

    public Contact addContact(Contact contact, String userId) throws AddressBookException {
        return addContact(contact, userId, new Date());
    }

    public Contact addContact(Contact contact, String userId, Date modifiedDate) throws AddressBookException {
        try {
            // generate id
            if(contact.getId() == null){
                String uuid = UuidGenerator.getInstance().getUuid();
                contact.setId(uuid);
            }

            // set user id
            contact.setOwnerId(userId);

            //set created/modified date
            if(modifiedDate == null){
                contact.setCreatedTime(new Date());
                contact.setModifiedTime(new Date());
            }else{
                contact.setCreatedTime(modifiedDate);
                contact.setModifiedTime(modifiedDate);
            }

            // store
            AddressBookDao dao = (AddressBookDao)getDao();
            dao.insertContact(contact, userId);
            return contact;
        }
        catch (DaoException e) {
            throw new AddressBookException("Error adding contact", e);
        }
    }

    public Contact updateContact(Contact contact, String userId) throws AddressBookException {
        return updateContact(contact, userId, new Date());
    }

     public Contact updateContact(Contact contact, String userId, Date modifiedDate) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            //blake
            //have to deal with extension
            // new comment have to be generated, just in case extension is updated
            if(contact.getExtension()!=null && contact.getExtension().length() > 0){
                VCardUtils util = new VCardUtils();
                contact.setComments(util.notesExtensionGenerator(contact.getComments(), contact.getExtension()));
            }
            contact.setModified(true);
            if(modifiedDate == null){
                contact.setModifiedTime(new Date());
            }else{
                contact.setModifiedTime(modifiedDate);
            }
            
            dao.updateContact(contact, userId);

            return contact;
        }
        catch (DaoException e) {
            throw new AddressBookException("Error updating contact", e);
        }
    }

    public void moveContact(String contactId, String folderId, String userId) throws AddressBookException {
        try {
            Contact contact = getContact(contactId);
            contact.setFolderId(folderId);
            updateContact(contact, userId);
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Error finding contact " + contactId + ": " + e.toString());
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error updating folder for " + contactId + ": " + e.toString());
        }
    }

    public void approveContact(String contactId, String userId) throws AddressBookException {
        try {
            Contact contact = getContact(contactId);
            contact.setApproved(true);
            updateContact(contact, userId);
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Error finding contact " + contactId + ": " + e.toString());
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error updating approval status for " + contactId + ": " + e.toString());
        }
    }

    public void deleteContact(String contactId, String userId) throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            dao.deleteContact(contactId, userId);
        }
        catch (DaoException e) {
            throw new AddressBookException("Error deleting contact", e);
        }
    }

    /**
     * Filters email addresses to return addresses not registered in the module.
     * @param emails A comma separated sequence of addresses into InternetAddress objects. Addresses must follow RFC822 syntax.
     * @param userId The user ID of the user's contacts, use null for any user
     * @return A Collection of emails (String)
     */
    public Collection filterContactEmails(String emails, String userId) throws AddressBookException {
        try {
            // form array of emails
            InternetAddress[] addresses = InternetAddress.parse(emails);
            Map newEmailMap = new SequencedHashMap();
            for (int i=0; i<addresses.length; i++) {
                InternetAddress address = addresses[i];
                String emailAdd = address.getAddress();
                if (emailAdd != null && emailAdd.trim().length() > 0) {
                    newEmailMap.put(address.getAddress(), address.toString());
                }
            }

            // get registered emails
            String[] emailArray = (String[])newEmailMap.keySet().toArray(new String[0]);
            AddressBookDao dao = (AddressBookDao)getDao();
            Collection registeredEmails = dao.selectContactEmails(emailArray, userId);

            // filter registered emails
            for (Iterator i=newEmailMap.keySet().iterator(); i.hasNext();) {
                String email = (String)i.next();
                if (registeredEmails.contains(email)) {
                    i.remove();
                }
            }

            return newEmailMap.values();
        }
        catch (Exception e) {
            throw new AddressBookException("Error filtering contact emails", e);
        }
    }

    /**
     * Filters a Collection of email addresses to return addresses not registered in the module.
     * @param emailList A Collection email addresses (String)
     * @param userId The user ID of the user's contacts, use null for any user
     * @return A Collection of emails (String)
     */
    public Collection filterContactEmails(Collection emailList, String userId) throws AddressBookException {
        try {
            // form array of emails
            Map newEmailMap = new SequencedHashMap();
            for (Iterator i=emailList.iterator(); i.hasNext();) {
                String email = (String)i.next();
                try {
                    InternetAddress address = new InternetAddress(email);
                    String emailAdd = address.getAddress();
                    if (emailAdd != null && emailAdd.trim().length() > 0) {
                        newEmailMap.put(address.getAddress(), address.toString());
                    }
                }
                catch (AddressException e) {
                    ;
                }
            }

            // get registered emails
            String[] emailArray = (String[])newEmailMap.keySet().toArray(new String[0]);
            AddressBookDao dao = (AddressBookDao)getDao();
            Collection registeredEmails = dao.selectContactEmails(emailArray, userId);

            // filter registered emails
            for (Iterator i=newEmailMap.keySet().iterator(); i.hasNext();) {
                String email = (String)i.next();
                if (registeredEmails.contains(email)) {
                    i.remove();
                }
            }

            return newEmailMap.values();
        }
        catch (DaoException e) {
            throw new AddressBookException("Error filtering contact emails", e);
        }
    }

//-- Import methods

    /**
     *
     * @param folderId Optional, the folder to import the contacts into
     * @param csv CSV String to import.
     * @param customFieldMapping Leave null for default mapping (supporting Outlook Express and Outlook)
     * @param userId
     * @return number of contacts imported.
     */
    public int importCsv(String folderId, String csv, Object[][] customFieldMapping, String userId) {
        Object[][] fieldMappingArray = null;
        if (customFieldMapping != null) {
            fieldMappingArray = customFieldMapping;
        }
        else {
        // use default field mapping
            fieldMappingArray = new Object[][] {
                { "First Name", "firstName" },
                { "Middle Name", "middleName" },
                { "Last Name", "lastName" },
                { "Nickname", "nickName" },
                { "Name", "firstName" },
                { "E-mail Address", "email" },
                { "Mobile Phone", "mobile" },
                { "Business Street", "address" },
                { "Business City", "city" },
                { "Business Postal Code", "postcode" },
                { "Business State", "state" },
                { "Business Country/Region", "country" },
                { "Business Phone", "phone" },
                { "Business Fax", "fax" },
                { "Company", "company" },
                { "Job Title", "designation" },
            };
        }

        // create field map
        ArrayList fieldList = new ArrayList();
        HashMap fieldMap = new HashMap();
        for (int i=0; i<fieldMappingArray.length; i++) {
            Object[] mapping = fieldMappingArray[i];
            fieldMap.put(mapping[0], mapping[1]);
        }

        // parse csv
        AddressBookCsvParser parser = new AddressBookCsvParser(csv);
        Collection lines = parser.getLines();

        // get headings
        Iterator it = lines.iterator();
        if (!it.hasNext()) {
            return 0;
        }
        else {
            Collection tokens = (Collection)it.next();
            for(Iterator jt=tokens.iterator(); jt.hasNext();) {
                String token = (String)jt.next();
                String propertyName = (String)fieldMap.get(token);
                fieldList.add(propertyName);
            }
        }

        // iterate thru data and create contacts
        int contactCount = 0;
        while(it.hasNext()) {
            Contact newContact = new Contact();
            Collection tokens = (Collection)it.next();
            int tokenIndex = 0;
            try {
                for(Iterator jt=tokens.iterator(); jt.hasNext(); tokenIndex++) {
                    String token = (String)jt.next();
                    String propertyName = (String)fieldList.get(tokenIndex);
                    if (propertyName != null && propertyName.trim().length() != 0) {
                        PropertyUtils.setProperty(newContact, propertyName, token);
                    }
                }
                newContact.setApproved(false);
                newContact.setFolderId(folderId);

                // check for existing contact
                boolean existing = checkForExistingContact(newContact);

                // create contact
                if (!existing) {
                    addContact(newContact, userId);
                    ++contactCount;
                }
            }
            catch (Exception e) {
                log.error("Error importing contact " + tokens + ": " + e.toString());
            }
        }
        return contactCount;
    }

    protected boolean checkForExistingContact(Contact newContact) throws AddressBookException {
        boolean existing = false;
        Collection matchingContactList = getContactList(newContact.getEmail(), null, null, null, null, null, false, 0, -1);
        for (Iterator jt=matchingContactList.iterator(); jt.hasNext();) {
            Contact c = (Contact)jt.next();
            existing = compareContactProperty(c.getEmail(), newContact.getEmail())
                    && compareContactProperty(c.getFirstName(), newContact.getFirstName())
                    && compareContactProperty(c.getLastName(), newContact.getLastName());
            if (existing)
                break;
        }
        return existing;
    }

    protected boolean compareContactProperty(String val1, String val2) {
        return (val1 == null && val2 == null) ||
                (val1 != null && val2 != null && val1.trim().equalsIgnoreCase(val2.trim()));
    }


//-- Admin and audit methods

    /**
     * Retrieves a Collection of all contact titles currently used.
     * @return A Collection of String objects.
     */
    public Collection getContactTitleList() throws AddressBookException {
        try {
            AddressBookDao dao = (AddressBookDao)getDao();
            Collection titleList = dao.selectContactTitles();
            return titleList;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error getting contact title list", e);
            throw new AddressBookException("Error getting contact title list", e);
        }
    }

    public String[] getCustomProperties() {
        return null;
    }

/*
// TODO: Import/export CSV
// TODO: Display name
// TODO: Custom fields
// TODO: Indexed search
// TODO: Sync methods

    public Collection readModifiedFolders(String userId) {
        return null;
    }

    public Collection readModifiedContacts(String userId) {
        return null;
    }

    public void writeModifiedFolders(String userId, Collection folderList) {

    }

    public void writeModifiedContacts(String userId, Collection contactList) {

    }
*/

    protected String getSearchUserId(String userId) {
        return userId;
    }

    protected Boolean getSearchStatus() {
        return null;
    }

	public SearchResult search(String query, int start, int rows, String userId) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Collection contactList = getContactList(query, null, null, getSearchUserId(userId), getSearchStatus(), "lastName", true, start, rows);
            for (Iterator i=contactList.iterator(); i.hasNext();) {
                Contact c = (Contact)i.next();
                SearchResultItem item = new SearchResultItem();
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, c.getDisplayName());
				StringBuffer desc = new StringBuffer();
				if(!(c.getPhone() == null || "".equals(c.getPhone())))
					desc.append(c.getPhone());
				if(!(c.getExtension() == null || "".equals(c.getExtension())))
					desc.append(" (" + Application.getInstance().getMessage("addressbook.label.ext") + ": " + c.getExtension() + ")");
				valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, desc.toString());
                item.setValueMap(valueMap);
                sr.add(item);
            }
            int count = getContactCount(query, null, null, null, getSearchStatus());
            sr.setTotalSize(count);
            return sr;
        }
        catch (Exception e) {
            log.error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    public SearchResult searchFullText(String s, int i, int i1, String s1) throws QueryException
    {
        return new SearchResult();
    }

	public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return search(query, start, rows, userId);
	}

	public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return new SearchResult();
	}

	public boolean isSearchSupported()
    {
        return true;
    }

    public boolean isFullTextSearchSupported()
    {
        return false;
    }

      //Added by blake
    public Collection getUpdatedContactList(String ownerId, Date sinceDate, Date untilDate) throws DaoException {
        AddressBookDao dao = (AddressBookDao) getDao();
        return dao.getUpdatedContactList(ownerId, sinceDate, untilDate);
    }

    public Collection getNewContactList(String ownerId, Date sinceDate, Date untilDate) throws DaoException {
        AddressBookDao dao = (AddressBookDao) getDao();
        return dao.getNewContactList(ownerId, sinceDate, untilDate);
    }

    public Collection getContactsByCriteria(Contact contact) throws DaoException {
        AddressBookDao dao = (AddressBookDao) getDao();
        return dao.getContactsByCriteria(contact);
    }

    public Collection getDeletedContacts(String ownerId, Date sinceDate, Date untilDate) {
        AddressBookDao dao = (AddressBookDao) getDao();
        try {
            return dao.getDeletedContactsKeys(ownerId, sinceDate, untilDate);
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }    
}
