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
public class SalutationForm extends Form {
	protected TextField tf_SalutationText;
	protected SelectBox sel_IsArchived;
	protected Button submit;
	protected Button cancel;

	private Label lbSalutationText;
	private Label lbIsArchived;
	
	private String salutationID;
	
	private String type; // possible values: "View", "Add", "Edit"
	public static final String FORWARD_CANCEL = "cancel";	
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. SalutationForm");
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getSalutationID() {
		return salutationID;
	}
	
	public void setSalutationID(String string) {
		salutationID = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.productName","Product Name")+":"));
		if (type.equals("View")) {
			lbSalutationText = new Label("lbSalutationText", "");
			addChild(lbSalutationText);
		} else {
			tf_SalutationText = new TextField("tf_SalutationText");
			tf_SalutationText.setMaxlength("50");
			tf_SalutationText.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_SalutationText.addChild(vne);
			addChild(tf_SalutationText);
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
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
			addChild(cancel);
	 		addChild(submit);
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
			myForward = addSalutation();
		} else if (type.equals("Edit")) {
			myForward = editSalutation();
		}
		initForm();
		return myForward;
	}
	
	private Forward addSalutation() {
		Application application = Application.getInstance();
		SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
		
		Salutation salu = new Salutation();
		UuidGenerator uuid = UuidGenerator.getInstance();
		salutationID = uuid.getUuid();
		salu.setSalutationID(salutationID);
		salu.setSalutationText((String) tf_SalutationText.getValue());
		salu.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(salu)) {
			return new Forward("salutationDuplicate");
		}
		
		module.addSalutation(salu);
		
		return new Forward("salutationAdded");
	}
	
	private Forward editSalutation() {
		Application application = Application.getInstance();
		SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
		
		Salutation salu = module.getSalutation(salutationID);
		salu.setSalutationText((String) tf_SalutationText.getValue());
		salu.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(salu)) {
			return new Forward("salutationDuplicate");
		}
		
		module.updateSalutation(salu);
		
		return new Forward("salutationUpdated");
	}
	
	public void populateView() {
		Application application = Application.getInstance();
		SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
		Salutation salu          = module.getSalutation(salutationID);
		
		lbSalutationText.setText(salu.getSalutationText());
		lbIsArchived.setText((String) DisplayConstants.getYesNoMap().get(salu.getIsArchived()));
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
		Salutation salu          = module.getSalutation(salutationID);
		
		tf_SalutationText.setValue(String.valueOf(salu.getSalutationText()));
		sel_IsArchived.setSelectedOptions(new String[] { salu.getIsArchived() });
	}
	
	public String getDefaultTemplate() {
		return "sfa/Salutation_Form";
	}
}
