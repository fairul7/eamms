package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.Validator;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

public class ContactNewForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    protected SelectBox title;
    protected TextField newTitle;
    protected TextField firstName;
    protected TextField middleName;
    protected TextField lastName;
    protected TextField nickName;
    protected TextField designation;
    protected TextField company;
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
    protected Button submit;
    protected Button cancel;
    protected Validator validFirstName;
    protected ValidatorEmail validEmail;

    public ContactNewForm() {
    }

    public String getDefaultTemplate() {
    	return "addressbook/abNewContact";
    }
     
    public ContactNewForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    protected void initForm() {
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

            contact.setApproved(true);

            AddressBookModule dm = getModule();
            dm.addContact(contact, userId);

            initForm();
            return new Forward(FORWARD_SUCCESS);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to add contact: " + e.toString(), e);
            return new Forward(FORWARD_ERROR);
        }
    }

    private String type;

    public String getType() {
        return AddressBookModule.MODULE_NAME;
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

	public boolean isEditMode() {
		return false;
	}
}
