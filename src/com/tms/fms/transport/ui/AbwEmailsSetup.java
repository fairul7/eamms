package com.tms.fms.transport.ui;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorEmail;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.fms.engineering.model.EngineeringModule;

public class AbwEmailsSetup extends Form
{
	public static final int NUM_OF_EMAIL = 5;
	public static final String TASK_ID_FOR_ABW_EMAIL_SETUP = "abwEmlStp";
	
	private TextField[] email;
	private Button submit;
	private Button cancel;
	
	public void init() 
	{
		initForm();
	}
	
	public void onRequest(Event event)
	{
		init();
		loadForm();
	}
	
	public void initForm() 
	{
		removeChildren();
		
		email = new TextField[AbwEmailsSetup.NUM_OF_EMAIL];
		for(int i = 0; i < AbwEmailsSetup.NUM_OF_EMAIL; i++)
		{
			email[i] = new TextField("email"+i);
			email[i].addChild(new ValidatorEmail("validEmail"+i, Application.getInstance().getMessage("security.label.invalidEmail","Invalid Email")));
			addChild(email[i]);
		}
		
		submit = new Button("submit", Application.getInstance().getMessage("fms.administration.btn.submit"));
		addChild(submit);
		
		cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("fms.administration.btn.cancel"));
		addChild(cancel);
	}
	
	public void loadForm() 
	{
		EngineeringModule em = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		DefaultDataObject obj = em.getAbWEmailSetup();
		if(obj != null)
		{
			for(int i = 0; i < AbwEmailsSetup.NUM_OF_EMAIL; i++)
			{
				email[i].setValue(obj.getProperty("email"+(i+1)) != null ? obj.getProperty("email"+(i+1)) : "");
			}
		}
	}
	
	public Forward onValidate(Event event) 
	{
		String buttonClicked = findButtonClicked(event);
		if(buttonClicked != null && buttonClicked.equals(submit.getAbsoluteName()))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(UuidGenerator.getInstance().getUuid());
			obj.setProperty("taskId", AbwEmailsSetup.TASK_ID_FOR_ABW_EMAIL_SETUP);
			for(int i = 0; i < AbwEmailsSetup.NUM_OF_EMAIL; i++)
			{
				obj.setProperty("email"+(i+1), email[i].getValue());
			}
			
			EngineeringModule em = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			em.deleteAbWEmailSetup();
			em.insertAbWEmailSetup(obj);
			
			return new Forward("updateSuccessfully");
		}
		
		return null;
	}
	
	public String getDefaultTemplate() 
	{
		return "fms/transport/abwEmailsSetupTemplate";
	}
}
