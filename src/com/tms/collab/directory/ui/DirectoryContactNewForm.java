package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.Validator;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;
import com.tms.collab.messaging.model.*;
import com.tms.collab.messaging.model.Util;

import java.util.*;

public class DirectoryContactNewForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_PENDING = "pending";
    public static final String FORWARD_ERROR = "error";

    protected SelectBox title;
    protected TextField newTitle;
    protected TextField firstName;
    protected TextField middleName;
    protected TextField lastName;
    protected TextField nickName;
    protected TextField designation;
    protected TextField company;
    protected CompanySelectBox companySelectBox;
    protected TextBox address;
    protected TextField city;
    protected TextField state;
    protected TextField postcode;
    protected CountrySelectBox country;
    protected TextField email;
    protected TextField phone;
    protected TextField extension;
    protected TextField fax;
    protected TextField mobile;
    protected TextBox comments;
    protected FolderSelectBox folderSelectBox;
    protected CheckBox approved;
    protected Button submit;
    protected Button cancel;
    protected Validator validFirstName;
    protected ValidatorEmail validEmail;

    public DirectoryContactNewForm() {
    }
    
    public String getDefaultTemplate() {
    	return "addressbook/bdNewContact";
    }

    public DirectoryContactNewForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    protected void initForm() {
        Application app = Application.getInstance();
        SecurityService security = (SecurityService)app.getService(SecurityService.class);

        removeChildren();
        setColumns(2);
        setMethod("POST");

        Label lb1 = new Label("lb1", Application.getInstance().getMessage("addressbook.label.title","Title"));
        Label lb2 = new Label("lb2", Application.getInstance().getMessage("addressbook.label.firstName_*","First Name *"));
        Label lb3 = new Label("lb3", Application.getInstance().getMessage("addressbook.label.middleName","Middle Name"));
        Label lb4 = new Label("lb4", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
        Label lb21 = new Label("lb21", Application.getInstance().getMessage("addressbook.label.nickName","Nick Name"));
        Label lb22 = new Label("lb22", Application.getInstance().getMessage("addressbook.label.designation","Designation"));
        Label lb23 = new Label("lb23", Application.getInstance().getMessage("addressbook.label.company","Company"));
        Label lb24 = new Label("lb24", Application.getInstance().getMessage("addressbook.label.registeredCompany","Registered Company"));
        Label lb5 = new Label("lb5", Application.getInstance().getMessage("addressbook.label.address","Address"));
        Label lb6 = new Label("lb6", Application.getInstance().getMessage("addressbook.label.city","City"));
        Label lb7 = new Label("lb7", Application.getInstance().getMessage("addressbook.label.state","State"));
        Label lb8 = new Label("lb8", Application.getInstance().getMessage("addressbook.label.postcode","Postcode"));
        Label lb9 = new Label("lb9", Application.getInstance().getMessage("addressbook.label.country","Country"));
        Label lb10 = new Label("lb10", Application.getInstance().getMessage("addressbook.label.email","Email"));
        Label lb11 = new Label("lb11", Application.getInstance().getMessage("addressbook.label.phone","Phone"));
        Label lb12 = new Label("lb12", Application.getInstance().getMessage("addressbook.label.extension","Extension"));
        Label lb13 = new Label("lb13", Application.getInstance().getMessage("addressbook.label.fax","Fax"));
        Label lb14 = new Label("lb14", Application.getInstance().getMessage("addressbook.label.mobile","Mobile"));
        Label lb15 = new Label("lb15", Application.getInstance().getMessage("addressbook.label.folder","Folder"));
        Label lb16 = new Label("lb16", Application.getInstance().getMessage("addressbook.label.comments","Comments"));
        Label lbApproved = new Label("lbApproved", Application.getInstance().getMessage("addressbook.label.approved","Approved"));
        Label lb17 = new Label("lb17", "");

        title = new SelectBox("title");
        title.addOption("", "");
/*
        title.addOption("Dr", Application.getInstance().getMessage("addressbook.label.dr","Dr"));
        title.addOption("Miss", Application.getInstance().getMessage("addressbook.label.miss","Miss"));
        title.addOption("Mr", Application.getInstance().getMessage("addressbook.label.mr","Mr"));
        title.addOption("Mrs", Application.getInstance().getMessage("addressbook.label.mrs","Mrs"));
        title.addOption("Ms", Application.getInstance().getMessage("addressbook.label.ms","Ms"));
        title.addOption("Prof", Application.getInstance().getMessage("addressbook.label.prof","Prof"));
*/
        // get existing contact titles
        try {
            AddressBookModule dm = getModule();
            Collection titleList = dm.getContactTitleList();
            for (Iterator i=titleList.iterator(); i.hasNext();) {
                String t = (String)i.next();
                title.addOption(t, t);
            }
        }
        catch (AddressBookException e) {
            // ignore
        }

        newTitle = new TextField("newTitle");
        newTitle.setSize("5");
        firstName = new TextField("firstName");
        validFirstName = new ValidatorNotEmpty("validFirstName");
        firstName.addChild(validFirstName);
        middleName = new TextField("middleName");
        lastName = new TextField("lastName");
        nickName = new TextField("nickName");
        designation = new TextField("designation");
        company = new TextField("company");
        companySelectBox = new CompanySelectBox("companySelectBox");
        address = new TextBox("address");
        city = new TextField("city");
        state = new TextField("state");
        postcode = new TextField("postcode");
        country = new CountrySelectBox("country");
        email = new TextField("email");
        validEmail = new ValidatorEmail("validEmail");
        email.addChild(validEmail);
        phone = new TextField("phone");
        extension = new TextField("extension");
        fax = new TextField("fax");
        mobile = new TextField("mobile");
        comments = new TextBox("comments");
        folderSelectBox = new FolderSelectBox("folderSelectBox");
        folderSelectBox.setType(getType());

        approved = new CheckBox("approved");
        try {
            if (security.hasPermission(getWidgetManager().getUser().getId(), DirectoryModule.PERMISSION_MANAGE_CONTACTS, null, null)) {
                approved.setChecked(true);
            }
            else {
                approved.setChecked(false);
                approved.setHidden(true);
                lbApproved.setHidden(true);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error getting contact permission", e);
            approved.setChecked(false);
            approved.setHidden(true);
        }


		if (isEditMode()) {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.update","Update"));
		} else {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.submit","Submit"));
		}
        cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("addressbook.label.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);
        Panel titlePanel = new Panel("titlePanel");
        titlePanel.addChild(title);
        titlePanel.addChild(newTitle);

        addChild(lb15);
        addChild(folderSelectBox);
        addChild(lb1);
        addChild(titlePanel);
        addChild(lb2);
        addChild(firstName);
        addChild(lb3);
        addChild(middleName);
        addChild(lb4);
        addChild(lastName);
        addChild(lb21);
        addChild(nickName);
        addChild(lb10);
        addChild(email);
        addChild(lb14);
        addChild(mobile);
        addChild(lb22);
        addChild(designation);
        addChild(lb24);
        addChild(companySelectBox);
        addChild(lb23);
        addChild(company);
        addChild(lb5);
        addChild(address);
        addChild(lb6);
        addChild(city);
        addChild(lb7);
        addChild(state);
        addChild(lb8);
        addChild(postcode);
        addChild(lb9);
        addChild(country);
        addChild(lb11);
        addChild(phone);
        addChild(lb12);
        addChild(extension);
        addChild(lb13);
        addChild(fax);
        addChild(lb16);
        addChild(comments);
        addChild(lbApproved);
        addChild(approved);
        addChild(lb17);
        addChild(buttonPanel);

    }

    public Forward onValidate(Event event) {
        try {
            String userId = getWidgetManager().getUser().getId();
            Contact contact = new Contact();

            String t = (String)newTitle.getValue();
            if (t == null || t.trim().length() == 0) {
                Map titleMap = title.getSelectedOptions();
                if (titleMap != null && titleMap.size() > 0) {
                    t = (String)titleMap.keySet().iterator().next();
                }
            }
            contact.setTitle(t);
            contact.setFirstName((String)firstName.getValue());
            contact.setMiddleName((String)middleName.getValue());
            contact.setLastName((String)lastName.getValue());
            contact.setNickName((String)nickName.getValue());
            contact.setDesignation((String)designation.getValue());
            contact.setCompany((String)company.getValue());

            Map companyMap = companySelectBox.getSelectedOptions();
            if (companyMap != null && companyMap.size() > 0) {
                String companyId = (String)companyMap.keySet().iterator().next();
                contact.setCompanyId(companyId);
            }

            contact.setAddress((String)address.getValue());
            contact.setCity((String)city.getValue());
            contact.setState((String)state.getValue());
            contact.setPostcode((String)postcode.getValue());
            contact.setCountry((String)country.getValue());
            contact.setEmail((String)email.getValue());
            contact.setPhone((String)phone.getValue());
            contact.setExtension((String)extension.getValue());
            contact.setFax((String)fax.getValue());
            contact.setMobile((String)mobile.getValue());
            contact.setComments((String)comments.getValue());

            contact.setOwnerId(userId);

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String folderId = (String)selectedMap.keySet().iterator().next();
                contact.setFolderId(folderId);
            }

            contact.setApproved(approved.isChecked());

            AddressBookModule dm = getModule();
            contact = dm.addContact(contact, userId);
			User user = getWidgetManager().getUser();
            Log.getLog(getClass()).write(new Date(), null, user.getId(), "kacang.services.log.directory.CreateContact", "New contact " + contact.getName() + " was created by user " + user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());

            initForm();
            if (contact.isApproved()) {
                return new Forward(FORWARD_SUCCESS);
            }
            else {
                // sending notification to approver
                sendNotificationForApproval(event,contact); // added on March 10, 2006
                return new Forward(FORWARD_PENDING);
            }
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to add contact: " + e.toString(), e);
            return new Forward(FORWARD_ERROR);
        }
    }

    private String type;

    public String getType() {
        return DirectoryModule.MODULE_NAME;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected AddressBookModule getModule() {
        return com.tms.collab.directory.ui.Util.getModule(this);
    }

    protected String getUserId() {
        return com.tms.collab.directory.ui.Util.getUserId(this);
    }
     protected void sendNotificationForApproval(Event evt,Contact contact) {
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
                  try{
                      SecurityService security = (SecurityService)app.getService(SecurityService.class);
                      String permissionId = "com.tms.collab.directory.ManageContacts";
                      Collection users = security.getUsersByPermission(permissionId,Boolean.TRUE,null,false,0,-1);
                      int size = users.size();
                      if(users != null && size > 0){
                          memoList = new ArrayList(size);
                          for (Iterator i = users.iterator(); i.hasNext();) {
                              User l_user = (User) i.next();
                              if (!userId.equals(l_user.getId())) {
                                      intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(l_user.getId());
                                      if (intranetAccount != null) {
                                          String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                                          memoList.add(add);
                                      }
                              }
                          }
                      }
                  }catch(Exception e){
                      e.printStackTrace();
                  }
                  if (memoList.size() > 0)
                      message.setToIntranetList(memoList);

                  message.setSubject("Contacts Approval Required : " + contact.getFirstName()+" "+contact.getLastName());

                  String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
                          "</head><body>" +
                          "<style>" +
                          ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                          "</style>" +
                          "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
                          "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
                          "<b><U>" +
                          " Contact Details "+
                          "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
                          "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
                          "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
                          "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                          "<b>" + app.getMessage("addressbook.label.firstName", "First Name") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + contact.getFirstName() + "</td></tr>" +
                          "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                          "<b>" + app.getMessage("addressbook.label.lastName", "Last Name") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + contact.getLastName() + "</td></tr>" +
                          "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("security.label.email", "Email") + "</b></td>" +
                          "<td class=\"contentBgColorMail\">" + contact.getEmail() + "</td>" +
                          "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("addressbook.label.phone", "Phone") + "</b></td>" +
                          "<td class=\"contentBgColorMail\">" + contact.getPhone() + "</td>" +
                          "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("addressbook.label.extension", "Extension") + "</b></td><td class=\"contentBgColorMail\">" +
                          contact.getExtension() + "</td></tr>" +
                          "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("addressbook.label.mobile", "Mobile") + "</b></td><td class=\"contentBgColorMail\">" +
                          contact.getMobile() + "</td></tr>";

                  String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
                          "</tr></table><p>&nbsp; </p></body></html>";

                 String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() +
                        "/ekms/addressbook/bdContactApprovalList.jsp\"/>" + app.getMessage("calendar.label.clickToView", "Click here to view") + "</a>" +
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

	public boolean isEditMode() {
		return false;
	}
}
