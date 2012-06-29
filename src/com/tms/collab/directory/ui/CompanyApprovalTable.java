package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.*;

import com.tms.collab.directory.model.*;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.IntranetAccount;

public class CompanyApprovalTable extends Table implements AddressBookWidget {

    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_DELETED = "deleted";
    public static final String FORWARD_APPROVED = "approved";

    protected ContactTableModel tableModel;

    public CompanyApprovalTable() {
    }

    public CompanyApprovalTable(String name) {
        super(name);
    }

    public void init() {
        setWidth("100%");
        tableModel = new ContactTableModel();
        setModel(tableModel);
    }

    public void setQuery(String query) {
        TextField q = (TextField)getModel().getFilter("query").getWidget();
        q.setValue(query);
    }

    private String type;

    public String getType() {
        return CompanyModule.MODULE_NAME;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected AddressBookModule getModule() {
        return Util.getModule(this);
    }

    protected String getUserId() {
        return Util.getUserId(this);
    }

    public class ContactTableModel extends TableModel {

        public ContactTableModel() {

            // add columns
            TableColumn companyName = new TableColumn("company", Application.getInstance().getMessage("addressbook.label.company","Company"));
            companyName.setUrlParam("id");
            companyName.setFormat(new ContactTableNameFormatter());
            addColumn(companyName);

            TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
            addColumn(email);

            TableColumn phone= new TableColumn("phone", Application.getInstance().getMessage("addressbook.label.phone","Phone"));
            addColumn(phone);

            TableColumn owner = new TableColumn("ownerId", Application.getInstance().getMessage("addressbook.label.owner","Owner"));
            owner.setFormat(new ContactTableOwnerFormatter());
            owner.setSortable(false);
            addColumn(owner);

            // add actions
            try {
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                String userId = getWidgetManager().getUser().getId();
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_COMPANIES, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("approve", Application.getInstance().getMessage("addressbook.label.approve","Approve")));
                }
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("delete", Application.getInstance().getMessage("addressbook.label.delete","Delete"), Application.getInstance().getMessage("addressbook.label.deletePrompt","Delete selected companies?")));
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error adding table actions: " + e.toString(), e);
            }

            // add filters
            TableFilter query = new TableFilter("query");
            addFilter(query);

        }

        public Collection getTableRows() {
            try {
                // get filter values
                String query = (String)getFilterValue("query");
                String ownerId = null;
                Boolean approved = Boolean.FALSE;

                // invoke module
                AddressBookModule dm = getModule();
                Collection rows = dm.getContactList(query, null, null, ownerId, approved, getSort(), isDesc(), getStart(), getRows());
                return rows;
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve companies: " + e.toString(), e);
                throw new RuntimeException("Unable to retrieve companies: " + e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // get filter values
                String query = (String)getFilterValue("query");
                String ownerId = null;
                Boolean approved = Boolean.FALSE;

                // invoke module
                AddressBookModule dm = getModule();
                int count = dm.getContactCount(query, null, null, ownerId, approved);
                return count;
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve company count: " + e.toString(), e);
                throw new RuntimeException("Unable to retrieve company count: " + e.toString());
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            try {
                AddressBookModule dm = getModule();
                String userId = getWidgetManager().getUser().getId();

                if ("approve".equals(action)) {
                    // approve
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            // approve contact
                            dm.approveContact(selectedKeys[i], userId);

                            // sending notification to user after approval
                            sendNotificationAfterApproval(event,selectedKeys[i]); // added on March 10, 2006
                        }
                        catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error approving company " + selectedKeys[i] + ": " + e.toString());
                        }
                    }
                    return new Forward(FORWARD_APPROVED);
                }
                else if ("delete".equals(action)) {
                    // delete
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            // delete contact
                            dm.deleteContact(selectedKeys[i], userId);
                        }
                        catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error deleting contact " + selectedKeys[i] + ": " + e.toString());
                        }
                    }
                    return new Forward(FORWARD_DELETED);
                }
                else {
                    return null;
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                return new Forward(FORWARD_ERROR);
            }
        }
    }

    protected void sendNotificationAfterApproval(Event evt,String contactId) {
          try {
              MessagingModule mm;
              User user;
              SmtpAccount smtpAccount;
              Message message;

              mm = com.tms.collab.messaging.model.Util.getMessagingModule();
              user = getWidgetManager().getUser();
              String userId = user.getId();
              smtpAccount = mm.getSmtpAccountByUserId(user.getId());

              // construct the message to send
              message = new Message();
              message.setMessageId(UuidGenerator.getInstance().getUuid());
              IntranetAccount intranetAccount;
              intranetAccount = com.tms.collab.messaging.model.Util.getMessagingModule().getIntranetAccountByUserId(userId);
              message.setFrom(intranetAccount.getFromAddress());
              List memoList = null;
              Application app = Application.getInstance();
              Contact contact=null;
              try{
                  AddressBookModule dm = getModule();
                  contact = dm.getContact(contactId);
                  if(contact != null ){
                      memoList = new ArrayList(1);
                          String ownerId  = contact.getOwnerId();
                          if (ownerId != null && !"".equals(ownerId)) {
                                  intranetAccount = com.tms.collab.messaging.model.Util.getMessagingModule().getIntranetAccountByUserId(ownerId);
                                  if (intranetAccount != null) {
                                      String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                                      memoList.add(add);
                                  }
                          }
                  }
              }catch(Exception e){
                  e.printStackTrace();
              }
              if (memoList.size() > 0)
                  message.setToIntranetList(memoList);

              message.setSubject("Registered Companies Approved : " + contact.getFirstName());

              String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
                      "</head><body>" +
                      "<style>" +
                      ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                      "</style>" +
                      "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
                      "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
                      "<b><U>" +
                      " Company Details "+
                      "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
                      "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
                      "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
                      "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                      "<b>" + "Company Name " + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + contact.getFirstName() + "</td></tr>" +
                      "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("security.label.email", "Email") + "</b></td>" +
                      "<td class=\"contentBgColorMail\">" + contact.getEmail() + "</td>" +
                      "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("addressbook.label.phone", "Phone") + "</b></td>" +
                      "<td class=\"contentBgColorMail\">" + contact.getPhone() + "</td>";

              String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
                      "</tr></table><p>&nbsp; </p></body></html>";

             String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() +
                    "/ekms/addressbook/bdCompanyList.jsp\"/>" + app.getMessage("calendar.label.clickToView", "Click here to view") + "</a>" +
                    "</td></tr> ";
              message.setBody(temp + link + footer);
              message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
              message.setDate(new Date());
              mm.sendMessage(smtpAccount, message, user.getId(), false);
          }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error sending notification", e);
        }
    }
}
