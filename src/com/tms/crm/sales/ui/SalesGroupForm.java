package com.tms.crm.sales.ui;

import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.TextBox;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 29, 2004
 * Time: 3:35:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesGroupForm extends Form{

    public static final String DEFAULT_TEMPLATE="/sfa/salesgroupform";
    public static final String FORWARD_CANCEL = "cancel";
    private String id;
    protected TextField nameTF;
    protected TextBox descriptionTB;
    protected SfaUsersSelectBox userSelectBox;
    protected Button submit, cancel;
    public SalesGroupForm() {
    }

    public SalesGroupForm(String s) {
        super(s);
    }

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        nameTF = new TextField("nametf");
        nameTF.addChild(new ValidatorNotEmpty("notemptysalesgroupname"));
        descriptionTB = new TextBox("descriptionTB");
        userSelectBox = new SfaUsersSelectBox("sfauserselectbox");

		if (isEditMode()) {
			submit = new Button("submit",Application.getInstance().getMessage("sfa.label.update","Update"));
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
			addChild(cancel);
		} else {
       		submit = new Button("submit",Application.getInstance().getMessage("sfa.label.submit","Submit"));

		}

        addChild(submit);
        addChild(nameTF);
        addChild(descriptionTB);
        addChild(userSelectBox);
        userSelectBox.init();
        setMethod("POST");
    }

    public Forward onSubmit(Event event) {
		if (isEditMode()) {
			if(cancel.getAbsoluteName().equals(findButtonClicked(event))) {
				return new Forward(FORWARD_CANCEL);
			}
		}
        return super.onSubmit(event);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TextField getNameTF() {
        return nameTF;
    }

    public void setNameTF(TextField nameTF) {
        this.nameTF = nameTF;
    }

    public TextBox getDescriptionTB() {
        return descriptionTB;
    }

    public void setDescriptionTB(TextBox descriptionTB) {
        this.descriptionTB = descriptionTB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public SfaUsersSelectBox getUserSelectBox() {
        return userSelectBox;
    }

    public void setUserSelectBox(SfaUsersSelectBox userSelectBox) {
        this.userSelectBox = userSelectBox;
    }

	public boolean isEditMode() {
		return false;
	}

}
