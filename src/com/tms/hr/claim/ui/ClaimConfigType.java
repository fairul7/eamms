package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;
import com.tms.hr.claim.model.ClaimTypeObject;

import kacang.Application;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;

import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.UuidGenerator;


public class ClaimConfigType extends Form {
    // protected ValidatorMessage validType;
    protected Button insertButton;
    protected TextField typeTextField;
    protected TextField accountTextiField;

    public void init() {
    }
    
    
    public String getDefaultTemplate() {
    	return "claims/config_type";
    }

    public void onRequest(Event evt) {
        Application app = Application.getInstance();

        setColumns(2);
        setMethod("POST");

        Label typefield = new Label("typefield",
                "<b>" + app.getMessage("claims.type.name") +" *"+ "</b>");
        addChild(typefield);

        typeTextField = new TextField("typetextfield", "");
        typeTextField.addChild(new ValidatorNotEmpty("typeTextFieldNotEmpty", "Must not be empty"));
        addChild(typeTextField);

        Label accountfield = new Label("accountfield",
                        "<b>" + app.getMessage("claims.category.code") +" *"+ "</b>");
        addChild(accountfield);

        accountTextiField = new TextField("accounttextfield", "");
        accountTextiField.addChild(new ValidatorNotEmpty("AccountTextFieldNotEmpty", "Must not be empty"));
        addChild(accountTextiField);


        insertButton = new Button("insertButton",
                app.getMessage("claims.category.submit"));
        addChild(new Label("a", ""));
        addChild(insertButton);
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

        Application app = Application.getInstance();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(insertButton.getAbsoluteName())) {
            UuidGenerator uuid = UuidGenerator.getInstance();
            uuid.getUuid();

            ClaimTypeObject object = new ClaimTypeObject();
            object.setId(uuid.getUuid());
            object.setTypeName((String) typeTextField.getValue());
            object.setAccountcode((String)accountTextiField.getValue());
            boolean uniqueType = module.isUniqueType((String) typeTextField.getValue(), uuid.getUuid());
            boolean uniqueTypeCode = module.isUniqueTypeCode((String)accountTextiField.getValue(), uuid.getUuid());

            if (!("".equals(object.getTypeName())) && !("".equals(object.getAccountcode()))) {
            	 if (uniqueType && uniqueTypeCode) {
            		module.insertType(object);     
            	 } else{
            		 if (!uniqueType) { 
            			 typeTextField.setInvalid(true);
            		 } 
            		 if (!uniqueTypeCode) {
            			 accountTextiField.setInvalid(true);
            		 } 
            		 return new Forward("duplicated");
            	 }
            } else {
            	return null;
            }
            typeTextField.setValue("");
            accountTextiField.setValue("");
            return new Forward("submit");
        }
        return null;
    }    
}
