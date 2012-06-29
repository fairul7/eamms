/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.*;
import kacang.Application;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContactTypeForm extends Form {
	protected TextField tf_ContactTypeName;
	protected SelectBox sel_IsArchived;
	protected Button submit;
	protected Button cancel;

	private Label lbContactTypeName;
	private Label lbIsArchived;
	
	private String contactTypeID;
	
	private String type; // possible values: "View", "Add", "Edit"
	public static final String FORWARD_CANCEL = "cancel";
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. ContactTypeForm");
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getContactTypeID() {
		return contactTypeID;
	}
	
	public void setContactTypeID(String string) {
		contactTypeID = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.contactType","Contact Type")+":"));
		if (type.equals("View")) {
			lbContactTypeName = new Label("lbContactTypeName", "");
			addChild(lbContactTypeName);
		} else {
			tf_ContactTypeName = new TextField("tf_ContactTypeName");
			tf_ContactTypeName.setMaxlength("255");
			tf_ContactTypeName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_ContactTypeName.addChild(vne);
			addChild(tf_ContactTypeName);
		}
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.archived","Archived")+":"));
		if (type.equals("View")) {
			lbIsArchived = new Label("lbIsArchived", "");
			addChild(lbIsArchived);
		} else {
			sel_IsArchived = new SelectBox("sel_IsArchived");
			sel_IsArchived.addOption("0", Application.getInstance().getMessage("sfa.label.no","No"));
			sel_IsArchived.addOption("1", Application.getInstance().getMessage("sfa.label.yes","Yes"));
			addChild(sel_IsArchived);
		}
		
		if (!type.equals("View") && (!type.equals("Edit"))) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
			addChild(submit);
		} else if (type.equals("Edit")) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
			addChild(submit);
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cance","Cancel"));
			addChild(cancel);
		}
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Edit")) {
			populateEdit();
		}
	}
	
	public Forward onValidate(Event evt) {
		Forward myForward = null;
		if (type.equals("Edit")) {
			if (cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
				myForward= new Forward(FORWARD_CANCEL);
			}
		}
		if (type.equals("Add")) {
			myForward = addContactType();
		} else if (type.equals("Edit") && !cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
			myForward = editContactType();
		}
		initForm();

		return myForward;
	}
	
	private Forward addContactType() {
		Application application  = Application.getInstance();
		ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
		
		ContactType ct = new ContactType();
		UuidGenerator uuid = UuidGenerator.getInstance();
		contactTypeID = uuid.getUuid();
		ct.setContactTypeID(contactTypeID);
		ct.setContactTypeName((String) tf_ContactTypeName.getValue());
		ct.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(ct)) {
			return new Forward("contactTypeDuplicate");
		}
		
		module.addContactType(ct);
		
		return new Forward("contactTypeAdded");
	}
	
	private Forward editContactType() {
		Application application  = Application.getInstance();
		ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
		
		ContactType ct = module.getContactType(contactTypeID);
		ct.setContactTypeName((String) tf_ContactTypeName.getValue());
		ct.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(ct)) {
			return new Forward("contactTypeDuplicate");
		}
		
		module.updateContactType(ct);
		
		return new Forward("contactTypeUpdated");
	}
	
	public void populateView() {
		Application application  = Application.getInstance();
		ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
		ContactType ct           = module.getContactType(contactTypeID);
		
		lbContactTypeName.setText(ct.getContactTypeName());
		lbIsArchived.setText((String) DisplayConstants.getYesNoMap().get(ct.getIsArchived()));
	}
	
	public void populateEdit() {
		Application application  = Application.getInstance();
		ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
		ContactType ct           = module.getContactType(contactTypeID);
		
		tf_ContactTypeName.setValue(String.valueOf(ct.getContactTypeName()));
		sel_IsArchived.setSelectedOptions(new String[] { ct.getIsArchived() });
	}
	
	public String getDefaultTemplate() {
		return "sfa/ContactType_Form";
	}

	public Forward actionPerformed(Event event) {
		Forward forward;
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_CANCEL);
        else
            forward = super.actionPerformed(event);
        return forward;
	}
}
