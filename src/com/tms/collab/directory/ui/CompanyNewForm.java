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
import com.tms.collab.directory.model.*;
import com.tms.collab.messaging.model.*;

import java.util.*;

public class CompanyNewForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_PENDING = "pending";
    public static final String FORWARD_ERROR = "error";

    protected TextField company;
    protected TextBox address;
    protected TextField city;
    protected TextField state;
    protected TextField postcode;
    protected CountrySelectBox country;
    protected TextField email;
    protected TextField phone;
    protected TextField fax;
    protected TextBox comments;
    protected CheckBox approved;
    protected Button submit;
    protected Button cancel;
    protected Validator validCompany;
    protected ValidatorEmail validEmail;

    public String getDefaultTemplate() {
    	return "addressbook/bdNewCompany";
    }
    
    public CompanyNewForm() {
    }

    public CompanyNewForm(String name) {
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

        Label lb23 = new Label("lb23", Application.getInstance().getMessage("addressbook.label.company","Company") + " *");
        Label lb5 = new Label("lb5", Application.getInstance().getMessage("addressbook.label.address","Address"));
        Label lb6 = new Label("lb6", Application.getInstance().getMessage("addressbook.label.city","City"));
        Label lb7 = new Label("lb7", Application.getInstance().getMessage("addressbook.label.state","State"));
        Label lb8 = new Label("lb8", Application.getInstance().getMessage("addressbook.label.postcode","Postcode"));
        Label lb9 = new Label("lb9", Application.getInstance().getMessage("addressbook.label.country","Country"));
        Label lb10 = new Label("lb10", Application.getInstance().getMessage("addressbook.label.email","Email"));
        Label lb11 = new Label("lb11", Application.getInstance().getMessage("addressbook.label.phone","Phone"));
        Label lb13 = new Label("lb13", Application.getInstance().getMessage("addressbook.label.fax","Fax"));
        Label lb16 = new Label("lb16", Application.getInstance().getMessage("addressbook.label.comments","Comments"));
        Label lbApproved = new Label("lbApproved", Application.getInstance().getMessage("addressbook.label.approved","Approved"));
        Label lb17 = new Label("lb17", "");

        company = new TextField("company");
        validCompany = new ValidatorNotEmpty("validCompany");
        company.addChild(validCompany);
        address = new TextBox("address");
        city = new TextField("city");
        state = new TextField("state");
        postcode = new TextField("postcode");
        country = new CountrySelectBox("country");
        email = new TextField("email");
        validEmail = new ValidatorEmail("validEmail");
        email.addChild(validEmail);
        phone = new TextField("phone");
        fax = new TextField("fax");
        comments = new TextBox("comments");

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

		if(isEditMode()){
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.update","Update"));
		} else {
        	submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.submit","Submit"));
		}

		cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("addressbook.label.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);

        addChild(lb23);
        addChild(company);
        addChild(lb10);
        addChild(email);
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

            contact.setFirstName((String)company.getValue());
            contact.setCompany((String)company.getValue());
            contact.setAddress((String)address.getValue());
            contact.setCity((String)city.getValue());
            contact.setState((String)state.getValue());
            contact.setPostcode((String)postcode.getValue());
            contact.setCountry((String)country.getValue());
            contact.setEmail((String)email.getValue());
            contact.setPhone((String)phone.getValue());
            contact.setFax((String)fax.getValue());
            contact.setComments((String)comments.getValue());

            contact.setOwnerId(userId);

            contact.setApproved(approved.isChecked());

            AddressBookModule dm = getModule();
            contact = dm.addContact(contact, userId);
			User user = getWidgetManager().getUser();
            Log.getLog(getClass()).write(new Date(), null, user.getId(), "kacang.services.log.directory.CreateCompany", "Company " + contact.getCompany() + " created by user "+ user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());

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
            Log.getLog(getClass()).error("Unable to add company: " + e.toString(), e);
            return new Forward(FORWARD_ERROR);
        }
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
                  String permissionId = "com.tms.collab.directory.ManageCompanies";
                  Collection users = security.getUsersByPermission(permissionId,Boolean.TRUE,null,false,0,-1);
                  int size = users.size();
                  if(users != null && size > 0){
                      memoList = new ArrayList(size);
                      for (Iterator i = users.iterator(); i.hasNext();) {
                          User l_user = (User) i.next();
                          if (!userId.equals(l_user.getId())) {
                                  intranetAccount = com.tms.collab.messaging.model.Util.getMessagingModule().getIntranetAccountByUserId(l_user.getId());
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

              message.setSubject("Registered Companies Approval Required : " + contact.getFirstName());

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
                    "/ekms/addressbook/bdCompanyApprovalList.jsp\"/>" + app.getMessage("calendar.label.clickToView", "Click here to view") + "</a>" +
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
