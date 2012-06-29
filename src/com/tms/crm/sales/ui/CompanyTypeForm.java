package com.tms.crm.sales.ui;

import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 3:32:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeForm extends Form{
    private String id;

    protected TextField tf_type;
    protected SelectBox sel_IsArchived;
    protected Button submit , cancel;

    public static final String DEFAULT_TEMPLATE = "/sfa/companytypeform";
    public static final String FORWARD_CANCEL ="cancel";

    public CompanyTypeForm() {
    }

    public CompanyTypeForm(String name) {
        super(name);
    }

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.

        setMethod("POST");
        tf_type = new TextField("tf_type");
        tf_type.setMaxlength("255");
        tf_type.setSize("40");
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
        tf_type.addChild(vne);
        addChild(tf_type);
        sel_IsArchived = new SelectBox("sel_IsArchived");
        sel_IsArchived.addOption("0", Application.getInstance().getMessage("sfa.label.no","No"));
        sel_IsArchived.addOption("1", Application.getInstance().getMessage("sfa.label.yes","Yes"));
        addChild(sel_IsArchived);

		if (isEditMode()) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
			cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
			addChild(cancel);
		} else {
        	submit = new Button("submit",Application.getInstance().getMessage("sfa.label.submit","Submit"));
		}
        addChild(submit);
    }


	public Forward onSubmit(Event evt) {
		if (isEditMode()) {
			if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
				return new Forward(FORWARD_CANCEL);
			}
		}
		return super.onSubmit(evt);    //To change body of overridden methods use File | Settings | File Templates
	}

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextField getTf_type() {
        return tf_type;
    }

    public void setTf_type(TextField tf_type) {
        this.tf_type = tf_type;
    }

    public SelectBox getSel_IsArchived() {
        return sel_IsArchived;
    }

    public void setSel_IsArchived(SelectBox sel_IsArchived) {
        this.sel_IsArchived = sel_IsArchived;
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

	public boolean isEditMode() {
		return false;
	}

}
