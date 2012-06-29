/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimProject;
import com.tms.hr.claim.model.ClaimProjectModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class ClaimProjectForm extends Form 
{

    protected TextField tf_FkPcc;
    protected TextField tf_Name;
    protected TextBox rtb_Description;
    protected SelectBox sb_Status;
    protected Panel buttonPanel;
	protected Button bn_Submit;
	protected Button bn_Cancel;

	public static final String FORWARD_CANCEL = "cancel";

	
	public String getDefaultTemplate() {
		return "claims/config_project";
	}
	
	
	public void init()
	{
		setColumns(2);
        setMethod("POST");

//		addChild(new Label("l1", "Profit Cost Center"));
//		tf_FkPcc = new TextField("tf_FkPcc");
//		addChild(tf_FkPcc);

		addChild(new Label("l2", "<b>"+Application.getInstance().getMessage("claims.label.projectName","Project Name")+" * "+"</b>"));
		tf_Name= new TextField("tf_Name");
        tf_Name.addChild(new ValidatorNotEmpty("vne1","Must not be empty."));
		addChild(tf_Name);

		addChild(new Label("l3", "<b>"+Application.getInstance().getMessage("claims.label.projectDescription","Project Description")+"</b>"));
		rtb_Description = new TextBox("rtb_Description");
		rtb_Description.setCols("40");
		rtb_Description.setRows("5");
//		rtb_Description.setWidth(40);
//		rtb_Description.setRow(5);
		addChild(rtb_Description);
		
		addChild(new Label("l4", "<b>"+"Status"+"</b>"));
		sb_Status = new SelectBox("sb_Status");
		sb_Status.addOption("act",Application.getInstance().getMessage("claims.label.active","Active"));
		sb_Status.addOption("ina",Application.getInstance().getMessage("claims.label.inActive","In-active"));
		addChild(sb_Status);

        addChild(new Label("15",""));

		buttonPanel = new Panel("buttonPanel");

		if (isEditMode()) {
			bn_Submit = new Button("update", "Update");
			bn_Cancel = new Button("cancel", "Cancel");
			buttonPanel.addChild(bn_Submit);
			buttonPanel.addChild(bn_Cancel);
		} else {
			bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.category.submit","Submit"));
			buttonPanel.addChild(bn_Submit);
		}
		addChild(buttonPanel);
    }

	public Forward onValidate(Event evt) 
	{
		Application application = Application.getInstance();
		ClaimProjectModule module = (ClaimProjectModule)
		application.getModule(ClaimProjectModule.class);

		ClaimProject obj = new ClaimProject();
		UuidGenerator uuid = UuidGenerator.getInstance();
		obj.setId(uuid.getUuid());
		//obj.setFkPcc((String)tf_FkPcc.getValue());
		obj.setName((String)tf_Name.getValue());
		obj.setDescription((String)rtb_Description.getValue());
		obj.setStatus((String) sb_Status.getSelectedOptions().keySet().iterator().next());

      module.addObject(obj);
		removeChildren();
		init();

		//return super.onValidate(evt);
        return new Forward("submit");

    }


	public boolean isEditMode() {
		return false;
	}

}
