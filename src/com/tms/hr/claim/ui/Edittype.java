package com.tms.hr.claim.ui;

import java.util.Collection;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.claim.model.ClaimConfigTypeModule;
import com.tms.hr.claim.model.ClaimTypeObject;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 19, 2005
 * Time: 10:45:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class Edittype extends Form {
    private String id;
    private TextField typeNameText;
    private TextField accountcodeText;
	private Panel buttonPanel;
    private Button editButton;
	private Button cancel;

	public static final String FORWARD_CANCEL = "cancel";

    public void init() {
        setColumns(2);

        Application app = Application.getInstance();
        Label lbltype = new Label("typeLabel", "<span class='classRowLabel'>"+ app.getMessage("claims.type.name","Type") + "&nbsp;*</span>");
        lbltype.setAlign("right");
        addChild(lbltype);
        typeNameText = new TextField("typeNameText", "");        
        typeNameText.addChild(new ValidatorNotEmpty("typeNameTextNotEmpty", app.getMessage("claims.message.mustNotBeEmpty", "Must not be empty")));
        addChild(typeNameText);
        
        Label lblcode = new Label("accountcodelabel", "<span class='classRowLabel'>"+ app.getMessage("claims.category.code","Account Code") + "&nbsp;*</span>");
        lblcode.setAlign("right");
        addChild(lblcode);
        accountcodeText = new TextField("accountcodeText", "");
        accountcodeText.addChild(new ValidatorNotEmpty("accountcodeTextNotEmpty", app.getMessage("claims.message.mustNotBeEmpty", "Must not be empty")));
        addChild(accountcodeText);

		buttonPanel = new Panel("buttonPanel");

		editButton = new Button("editButton", app.getMessage("claims.category.update"));
        addChild(new Label("a", ""));
        buttonPanel.addChild(editButton);
		cancel = new Button ("cancel", app.getMessage("claims.category.cancel"));
		buttonPanel.addChild(cancel);
		
		addChild(buttonPanel);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        
        Application app = Application.getInstance();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

        ClaimTypeObject object = null;

        if (getId() != null) {
            object = module.selectTypeName(getId());

            typeNameText.setValue((String) object.getTypeName());
            accountcodeText.setValue((String)object.getAccountcode());
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        
        if ("".equals(typeNameText.getValue()) || "".equals(accountcodeText.getValue())){
        	return null;
        }
        
        Application app = Application.getInstance();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
        boolean uniqueType = module.isUniqueType((String) typeNameText.getValue(), getId());
        boolean uniqueTypeCode = module.isUniqueTypeCode((String)accountcodeText.getValue(),getId());

        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(editButton.getAbsoluteName())) {
       	 	if (uniqueType && uniqueTypeCode) {
       	 		module.editType((String) typeNameText.getValue(), getId(), (String)accountcodeText.getValue());
                return new Forward("edit");
       	 	} else{
       	 		if (!uniqueType) { 
       	 			typeNameText.setInvalid(true);
       	 		} 
       	 		if (!uniqueTypeCode) {
       	 			accountcodeText.setInvalid(true);
       	 		} 
       	 		return new Forward("duplicated");
       	 	}

        } else if (buttonClicked.equals(cancel.getAbsoluteName())) {
			return new Forward(FORWARD_CANCEL);
		}

        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
