package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.*;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Message;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.ui.WidgetManager;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import org.apache.commons.lang.StringUtils;

public class Util implements Serializable {

//-- Messaging Constants

    public static final String RECIPIENT_TO = "to";
    public static final String RECIPIENT_CC = "cc";
    public static final String RECIPIENT_BCC = "bcc";

    public static final String TO_ATTRIBUTE = MessagingModule.TO_ATTRIBUTE;
    public static final String CC_ATTRIBUTE = MessagingModule.CC_ATTRIBUTE;
    public static final String BCC_ATTRIBUTE = MessagingModule.BCC_ATTRIBUTE;

//-- Utility Methods

    public static AddressBookModule getModule(AddressBookWidget widget) {
        Application app = Application.getInstance();
        String type = widget.getType();
        if (DirectoryModule.MODULE_NAME.equals(type)) {
            return (DirectoryModule)app.getModule(DirectoryModule.class);
        }
        else if (CompanyModule.MODULE_NAME.equals(type)) {
            return (CompanyModule)app.getModule(CompanyModule.class);
        }
        else {
            return (AddressBookModule)app.getModule(AddressBookModule.class);
        }
    }

    public static String getUserId(AddressBookWidget widget) {
        String type = widget.getType();
        if (DirectoryModule.MODULE_NAME.equals(type)) {
            return null;
        }
        else if (CompanyModule.MODULE_NAME.equals(type)) {
            return null;
        }
        else {
            WidgetManager wm = widget.getWidgetManager();
            if (wm != null) {
                return widget.getWidgetManager().getUser().getId();
            }
            else {
                return null;
            }
        }
    }

    public static String getEmailAddressByIds(AddressBookModule module, String[] contactIdArray) throws AddressBookException {
        Collection contactList = module.getContactListById(contactIdArray, "email", false);
        StringBuffer addresses = new StringBuffer("");
        String domain = (DirectoryModule.class.equals(module.getClass())) ? MessagingModule.CONTACT_GROUP_DIRECTORY_EMAIL_DOMAIN : MessagingModule.CONTACT_GROUP_PERSONAL_EMAIL_DOMAIN;
        for (Iterator i=contactList.iterator(); i.hasNext();) {
            try {
                Contact contact = (Contact)i.next();
                if (!contact.isContactGroup()) {
                	
                	String tempDisplayName=  new String(contact.getDisplayName().getBytes(),"UTF-8");
                	
                	if(tempDisplayName !=null && tempDisplayName.substring(0, 2).indexOf("??")>=0)
                		tempDisplayName = contact.getDisplayName();
                    
                	//InternetAddress address = new InternetAddress(contact.getEmail(), tempDisplayName);
                    
                    
                   
                     //  addresses.append(address.toString());
                	
                	/*
                	 * in order to support UTF-8
                	 */
                       addresses.append("\""+tempDisplayName+"\" <"+contact.getEmail()+">");
                                                                                               
                
                }
                else {
                    String contactGroupName = contact.getFirstName();
                    contactGroupName = StringUtils.replace(contactGroupName, "\"", "\\\"");
                    String contactAddr = "\"" + contactGroupName  + "@" + domain + "\"";
                    InternetAddress address = new InternetAddress(contactAddr, null);
                    addresses.append(address.toString());
                }
                if (i.hasNext()) {
                    addresses.append(",");
                }
            }
            catch (UnsupportedEncodingException e) {
                ;
            }
        }
        return addresses.toString();
    }

    public static String getEmailAddressByFolders(AddressBookModule module, String[] folderIdArray) throws AddressBookException {
        StringBuffer addresses = new StringBuffer("");
        for (int i=0; i<folderIdArray.length; i++) {
            if (i > 0) {
                addresses.append(",");
            }
            Collection contactList = module.getContactList(null, folderIdArray[i], null, null, null, "email", false, 0, -1);
            String domain = (DirectoryModule.class.equals(module.getClass())) ? MessagingModule.CONTACT_GROUP_DIRECTORY_EMAIL_DOMAIN : MessagingModule.CONTACT_GROUP_PERSONAL_EMAIL_DOMAIN;
            for (Iterator iter=contactList.iterator(); iter.hasNext();) {
                try {
                    Contact contact = (Contact)iter.next();
                    if (!contact.isContactGroup()) {
                        InternetAddress address = new InternetAddress(contact.getEmail(), contact.getDisplayName());
                        addresses.append(address.toString());
                    }
                    else {
                        String contactGroupName = contact.getFirstName();
                        contactGroupName = StringUtils.replace(contactGroupName, "\"", "\\\"");
                        contactGroupName = StringUtils.replace(contactGroupName, "'", "\\'");
                        String contactAddr = "\"" + contactGroupName  + "@" + domain + "\"";
                        InternetAddress address = new InternetAddress(contactAddr, null);
                        addresses.append(address.toString());
                    }
                    if (iter.hasNext()) {
                        addresses.append(",");
                    }
                }
                catch (UnsupportedEncodingException e) {
                    ;
                }
            }
        }
        return addresses.toString();
    }

    /**
     * Returns Collection of InternetAddress objects
     * @param module
     * @param contactGroupName
     * @param userId
     * @return
     * @throws AddressBookException
     */
    public static List getEmailAddressesForContactGroup(AddressBookModule module, String contactGroupName, String userId) throws AddressBookException {
        List addressList = new ArrayList();
        HashSet emailSet = new HashSet(); // used to check for duplicates
        try {
            String ownerId = DirectoryModule.class.equals(module.getClass()) ? null : userId;
            Contact contactGroup = module.getContactGroupByName(contactGroupName, ownerId, Boolean.TRUE);

            // add contacts
            String[] idArray = contactGroup.getContactIdArray();
            Collection contactList = module.getContactListById(idArray, "firstName", false);
            for (Iterator i=contactList.iterator(); i.hasNext();) {
                Contact ct = (Contact)i.next();
                try {
                    InternetAddress ia = new InternetAddress(ct.getEmail(), ct.getDisplayName());
                    String email = ia.getAddress();
                    if (email != null && email.trim().length() > 0 && !emailSet.contains(email)) {
                        addressList.add(ia);
                        emailSet.add(ia.getAddress());
                    }
                }
                catch (UnsupportedEncodingException e) {
                    Log.getLog(Util.class).warn("Invalid email address " + ct.getEmail(), e);
                }
            }

            // add contacts
            String[] intranetIdArray = contactGroup.getIntranetIdArray();
            Collection userList = getIntranetUsersList(intranetIdArray);
            for (Iterator i=userList.iterator(); i.hasNext();) {
                User user = (User)i.next();
                try {
                    String email = user.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                    if (email != null && email.trim().length() > 0 && !emailSet.contains(email)) {
                        InternetAddress ia = new InternetAddress(email, user.getName());
                        addressList.add(ia);
                        emailSet.add(ia.getAddress());
                    }
                }
                catch (UnsupportedEncodingException e) {
                    Log.getLog(Util.class).warn("Invalid email address " + user.getUsername(), e);
                }
            }

            // add other emails
            String otherEmails = contactGroup.getContactGroupEmails();
            InternetAddress[] addressArray = InternetAddress.parse(otherEmails, false);
            for (int j=0; j<addressArray.length; j++) {
                InternetAddress ia = addressArray[j];
                String email = ia.getAddress();
                if (email != null && email.trim().length() > 0 && !addressList.contains(email)) {
                    addressList.add(ia);
                    emailSet.add(ia.getAddress());
                }

            }

        }
        catch (AddressException e) {
            Log.getLog(Util.class).warn("Error parsing addresses for contact group " + contactGroupName);
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(Util.class).warn("Error retrieving addresses for contact group " + contactGroupName, e);
        }
        return addressList;
    }

    /**
     * Converts contact group addresses into the corresponding addresses of its contacts.
     * @param messageToProcess
     * @return A copy of the message with exploded addresses
     */
    public static Message processContactGroupAddresses(Message messageToProcess, String userId) {
        try {
            List internetAddressList;
            Message message = (Message)messageToProcess.clone();

            // handle TO recipients
            List toList = (message.getToList() != null) ? new ArrayList(message.getToList()) : new ArrayList();
            List toIntranetList = (message.getToIntranetList() != null) ? new ArrayList(message.getToIntranetList()) : new ArrayList();
            internetAddressList = retrieveInternetAddressList(toList, userId);
            if (internetAddressList.size() > 0) {
                for (Iterator j=internetAddressList.iterator(); j.hasNext();) {
                    InternetAddress ia = (InternetAddress)j.next();
                    String addr = ia.toString();
                    String email = ia.getAddress();
                    if (email.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN) && !toIntranetList.contains(addr)) {
                        toIntranetList.add(addr);
                    }
                    else if (!toList.contains(addr)) {
                        toList.add(addr);
                    }
                }
            }
            message.setToList(toList);
            message.setToIntranetList(toIntranetList);

            // handle CC recipients
            List ccList = (message.getCcList() != null) ? new ArrayList(message.getCcList()) : new ArrayList();
            List ccIntranetList = (message.getCcIntranetList() != null) ? new ArrayList(message.getCcIntranetList()) : new ArrayList();
            internetAddressList = retrieveInternetAddressList(ccList, userId);
            if (internetAddressList.size() > 0) {
                for (Iterator j=internetAddressList.iterator(); j.hasNext();) {
                    InternetAddress ia = (InternetAddress)j.next();
                    String addr = ia.toString();
                    String email = ia.getAddress();
                    if (email.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN) && !ccIntranetList.contains(addr)) {
                        ccIntranetList.add(addr);
                    }
                    else if (!ccList.contains(addr)) {
                        ccList.add(addr);
                    }
                }
            }
            message.setCcList(ccList);
            message.setCcIntranetList(ccIntranetList);

            // handle BCC recipients
            List bccList = (message.getBccList() != null) ? new ArrayList(message.getBccList()) : new ArrayList();
            List bccIntranetList = (message.getBccIntranetList() != null) ? new ArrayList(message.getBccIntranetList()) : new ArrayList();
            internetAddressList = retrieveInternetAddressList(bccList, userId);
            if (internetAddressList.size() > 0) {
                for (Iterator j=internetAddressList.iterator(); j.hasNext();) {
                    InternetAddress ia = (InternetAddress)j.next();
                    String addr = ia.toString();
                    String email = ia.getAddress();
                    if (email.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN) && !bccIntranetList.contains(addr)) {
                        bccIntranetList.add(addr);
                    }
                    else if (!ccList.contains(addr)) {
                        bccList.add(addr);
                    }
                }
            }
            message.setBccList(bccList);
            message.setBccIntranetList(bccIntranetList);

            if (true) {
                Log.getLog(Util.class).debug("Message: " + message.getSubject() +
                        "\nto" + messageToProcess.getToList() + "->" + toList +
                        "\ntoIntranet" + messageToProcess.getToIntranetList() + "->" + toIntranetList +
                        "\ncc" + messageToProcess.getCcList() + "->" + ccList +
                        "\nccIntranet" + messageToProcess.getCcIntranetList() + "->" + ccIntranetList +
                        "\nbcc" + messageToProcess.getBccList() + "->" + bccList +
                        "\nbccIntranet" + messageToProcess.getBccIntranetList() + "->" + bccIntranetList);
            }

            return message;
        }
        catch (CloneNotSupportedException e) {
            // should not happen
            Log.getLog(Util.class).error("Unable to clone message", e);
            return messageToProcess;
        }
    }

    /**
     * Retrieves a List of InternetAddress objects from a List of messaging addresses.
     * This method will explode all contact group addresses.
     * @param addrList
     * @param userId
     * @return
     */
    private static List retrieveInternetAddressList(List addrList, String userId) {
        List internetAddressList = new ArrayList();
        for (Iterator i=addrList.iterator(); i.hasNext();) {
            String addrStr = (String)i.next();
            try {
                InternetAddress ia = new InternetAddress(addrStr);
                String address = ia.getAddress();
                if (address.startsWith("\"") && address.endsWith("\"")) {
                    address = address.substring(1, address.length()-1);
                }
                if (address.endsWith(MessagingModule.CONTACT_GROUP_DIRECTORY_EMAIL_DOMAIN)) {
                    String contactGroupName = address.substring(0, address.length() - (MessagingModule.CONTACT_GROUP_DIRECTORY_EMAIL_DOMAIN.length() + 1));
                    DirectoryModule mod = (DirectoryModule)Application.getInstance().getModule(DirectoryModule.class);
                    internetAddressList.addAll(Util.getEmailAddressesForContactGroup(mod, contactGroupName, null));
                    i.remove();
                }
                else if (address.endsWith(MessagingModule.CONTACT_GROUP_PERSONAL_EMAIL_DOMAIN)) {
                    String contactGroupName = address.substring(0, address.length() - (MessagingModule.CONTACT_GROUP_PERSONAL_EMAIL_DOMAIN.length() + 1));
                    AddressBookModule mod = (AddressBookModule)Application.getInstance().getModule(AddressBookModule.class);
                    internetAddressList.addAll(Util.getEmailAddressesForContactGroup(mod, contactGroupName, userId));
                    i.remove();
                }

            }
            catch (Exception e) {
                Log.getLog(Util.class).warn("Error retrieving email address for contact group", e);
            }
        }
        return internetAddressList;
    }


    public static String getIntranetAddresses(String[] usernameArray) {
        StringBuffer addresses = new StringBuffer("");
        for (int i=0; i<usernameArray.length; i++) {
            String addr = usernameArray[i] + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
            addresses.append(addr);
            if (i < (usernameArray.length-1)) {
                addresses.append(",");
            }
        }
        return addresses.toString();
    }

    /**
     *
     * @param userIdArray
     * @return Collection of User objects
     */
    public static Collection getIntranetUsersList(String[] userIdArray) {
        Collection userList = new ArrayList();
        try {
            Application app = Application.getInstance();
            SecurityService sec = (SecurityService)app.getService(SecurityService.class);
            if (userIdArray != null && userIdArray.length > 0) {
                DaoQuery query = new DaoQuery().addProperty(new OperatorIn("id", userIdArray, OperatorIn.OPERATOR_AND));
                userList = sec.getUsers(query, 0, -1, "firstName", false);
            }
        }
        catch (SecurityException e) {
            Log.getLog(Util.class).error("Error retrieving intranet users list", e);
        }
        return userList;
    }

/*
    public static void setMessagingRecipient(HttpServletRequest request, String addresses, String recipientType) {
        // validate type
        if (recipientType == null || (!recipientType.equals(RECIPIENT_TO) && !recipientType.equals(RECIPIENT_CC) && !recipientType.equals(RECIPIENT_BCC))) {
            recipientType = RECIPIENT_TO;
        }

        // store session attribute
        if (RECIPIENT_CC.equals(recipientType)) {
            request.getSession().setAttribute(CC_ATTRIBUTE, addresses);
        }
        else if (RECIPIENT_BCC.equals(recipientType)) {
            request.getSession().setAttribute(BCC_ATTRIBUTE, addresses);
        }
        else {
            request.getSession().setAttribute(TO_ATTRIBUTE, addresses);
        }
    }
*/

    public static void setMessagingRecipient(HttpServletRequest request, String addresses, String recipientType) throws AddressBookException {
        String emails;
        if (RECIPIENT_CC.equals(recipientType)) {
            emails = (String)request.getSession().getAttribute(CC_ATTRIBUTE);
        }
        else if (RECIPIENT_BCC.equals(recipientType)) {
            emails = (String)request.getSession().getAttribute(BCC_ATTRIBUTE);
        }
        else {
            emails = (String)request.getSession().getAttribute(TO_ATTRIBUTE);
        }

        if (emails == null) {
            emails = "";
        }
        emails += ", " + addresses;

        try {
            // parse addresses
            Collection addressList = new ArrayList();
            HashSet emailSet = new HashSet(); // used to check for duplicates
            InternetAddress[] addressArray = InternetAddress.parse(emails, false);
            for (int i=0; i<addressArray.length; i++) {
                InternetAddress ia = addressArray[i];
                String email = ia.getAddress();
                if (email != null && email.trim().length() > 0 && !emailSet.contains(email)) {
                    addressList.add(ia);
                    emailSet.add(ia.getAddress());
                }
            }

            // generate email string
            StringBuffer newAddresses = new StringBuffer("");
            for (Iterator iter=addressList.iterator(); iter.hasNext();) {
                InternetAddress address = (InternetAddress)iter.next();
                newAddresses.append(address.toString());
                if (iter.hasNext()) {
                    newAddresses.append(", ");
                }
            }
            addresses = newAddresses.toString();

            // store in session
            if (RECIPIENT_CC.equals(recipientType)) {
                request.getSession().setAttribute(CC_ATTRIBUTE, addresses);
            }
            else if (RECIPIENT_BCC.equals(recipientType)) {
                request.getSession().setAttribute(BCC_ATTRIBUTE, addresses);
            }
            else {
                request.getSession().setAttribute(TO_ATTRIBUTE, addresses);
            }
        }
        catch (AddressException e) {
            Log.getLog(Util.class).error("Unable to parse email addresses", e);
            throw new AddressBookException("Unable to parse email addresses");
        }
    }


}
