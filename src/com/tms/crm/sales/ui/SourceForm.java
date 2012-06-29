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
public class SourceForm extends Form {
	protected TextField tf_SourceText;
	protected SelectBox sel_IsArchived;
	protected Button submit;
	protected Button cancel;

	private Label lbSourceText;
	private Label lbIsArchived;
	
	private String sourceID;
	
	private String type; // possible values: "View", "Add", "Edit"
	
	public static final String FORWARD_CANCEL = "cancel";
	
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. SourceForm");
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getSourceID() {
		return sourceID;
	}
	
	public void setSourceID(String string) {
		sourceID = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.productName","Product Name")+":"));
		if (type.equals("View")) {
			lbSourceText = new Label("lbSourceText", "");
			addChild(lbSourceText);
		} else {
			tf_SourceText = new TextField("tf_SourceText");
			tf_SourceText.setMaxlength("50");
			tf_SourceText.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_SourceText.addChild(vne);
			addChild(tf_SourceText);
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
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cancel","Update"));
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

	public Forward actionPerformed(Event event) {
		Forward forward;
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_CANCEL);
        else
            forward = super.actionPerformed(event);
        return forward;
	}
	
	public Forward onValidate(Event evt) {
		Forward myForward = null;
		if (type.equals("Add")) {
			myForward = addSource();
		} else if (type.equals("Edit")) {
			myForward = editSource();
		}
		initForm();
		return myForward;
	}
	
	private Forward addSource() {
		Application application = Application.getInstance();
		SourceModule module = (SourceModule) application.getModule(SourceModule.class);
		
		Source src = new Source();
		UuidGenerator uuid = UuidGenerator.getInstance();
		sourceID = uuid.getUuid();
		src.setSourceID(sourceID);
		src.setSourceText((String) tf_SourceText.getValue());
		src.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(src)) {
			return new Forward("sourceDuplicate");
		}
		
		module.addSource(src);
		
		return new Forward("sourceAdded");
	}
	
	private Forward editSource() {
		Application application = Application.getInstance();
		SourceModule module = (SourceModule) application.getModule(SourceModule.class);
		
		Source src = module.getSource(sourceID);
		src.setSourceText((String) tf_SourceText.getValue());
		src.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(src)) {
			return new Forward("sourceDuplicate");
		}
		
		module.updateSource(src);
		
		return new Forward("sourceUpdated");
	}
	
	public void populateView() {
		Application application = Application.getInstance();
		SourceModule module = (SourceModule) application.getModule(SourceModule.class);
		Source src          = module.getSource(sourceID);
		
		lbSourceText.setText(src.getSourceText());
		lbIsArchived.setText((String) DisplayConstants.getYesNoMap().get(src.getIsArchived()));
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		SourceModule module = (SourceModule) application.getModule(SourceModule.class);
		Source src          = module.getSource(sourceID);
		
		tf_SourceText.setValue(String.valueOf(src.getSourceText()));
		sel_IsArchived.setSelectedOptions(new String[] { src.getIsArchived() });
	}
	
	public String getDefaultTemplate() {
		return "sfa/Source_Form";
	}
}
