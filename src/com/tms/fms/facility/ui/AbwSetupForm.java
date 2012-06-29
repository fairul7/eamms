package com.tms.fms.facility.ui;

import java.util.Collection;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.facility.model.SetupModule;

public class AbwSetupForm extends Form
{
	public static final String EDIT_MODE="edit";
	private Label abwCodeLab;
	private TextField abwCode;
	private TextBox desc;
	private Button save;
	private Button cancel;
	
	private String mode;
	private String code;
	
	public void init()
	{
		initForm();
	}
	
	public void onRequest(Event evt)
	{
		init();
		if(mode != null && mode.equals(AbwSetupForm.EDIT_MODE))
		{
			loadForm();
		}
	}
	
	public void initForm()
	{
		Application app = Application.getInstance();
		removeChildren();
		String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
		
		abwCode = new TextField("abwCode");
		abwCode.addChild(new ValidatorNotEmpty("abwCodeNotEmpty", msgNotEmpty));
		abwCode.setMaxlength("10");
		abwCode.setSize("43");
		addChild(abwCode);
		
		abwCodeLab = new Label("abwCodeLab", "");
		abwCodeLab.setHidden(true);
		addChild(abwCodeLab);
		
		desc = new TextBox("desc");
		desc.addChild(new ValidatorNotEmpty("descNotEmpty", msgNotEmpty));
		desc.setMaxlength("255");
		addChild(desc);
		
		String msg = app.getMessage("fms.setup.btnSubmit");
		if(mode != null && mode.equals(AbwSetupForm.EDIT_MODE))
		{
			msg = app.getMessage("fms.setup.btnUpdate");
		}
		save = new Button("save", msg);
		addChild(save);
		
		cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("fms.setup.btnCancel"));
		addChild(cancel);
	}
	
	public void loadForm()
	{
		if(code != null && !code.equals(""))
		{
			SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			Collection col = sm.getAbwCodes(code);
			if(col != null && !col.isEmpty())
			{
				DefaultDataObject obj = (DefaultDataObject) col.iterator().next();
				abwCodeLab.setText((String)obj.getProperty("abw_code"));
				abwCode.setValue(obj.getProperty("abw_code"));
				desc.setValue(obj.getProperty("description"));
				
				abwCodeLab.setHidden(false);
				abwCode.setHidden(true);
				abwCode.removeChildren();
			}
		}
	}
	
	public Forward onSubmit(Event evt)
	{
		Forward fwd = super.onSubmit(evt);
		
		String buttonClicked = findButtonClicked(evt);
		if(buttonClicked != null && buttonClicked.equals(save.getAbsoluteName()))
		{
			if(abwCode.getValue() != null && !abwCode.getValue().equals(""))
			{
				SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
				Collection col = sm.getAbwCodes(abwCode.getValue().toString());
				if(col != null && !col.isEmpty())
				{
					abwCode.setInvalid(true);
					this.setInvalid(true);
					return new Forward("abwCodeDuplicated");
				}
			}
		}
		return fwd;
	}
	
	public Forward onValidate(Event evt)
	{
		Forward fwd = super.onValidate(evt);
		String buttonClicked = findButtonClicked(evt);
		if(buttonClicked != null && buttonClicked.equals(save.getAbsoluteName()))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setProperty("abw_code", abwCode.getValue());
			obj.setProperty("description", desc.getValue());
			
			SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			if(mode != null && mode.equals(AbwSetupForm.EDIT_MODE))
			{
				obj.setProperty("abw_code", abwCodeLab.getText());
				sm.updateAbwCode(obj);
				return new Forward("updateSuccessfully");
			}
			else
			{
				sm.insertAbwCode(obj);
				return new Forward("updateSuccessfully"); 
			}
		}
		
		return fwd;
	}
	
	public String getDefaultTemplate()
	{
		return "fms/facility/abwSetupFormTemplate";
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}
}
