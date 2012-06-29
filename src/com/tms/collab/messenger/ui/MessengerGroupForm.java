package com.tms.collab.messenger.ui;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.messenger.MessageModule;


public class MessengerGroupForm extends Form 
{
	
	protected MessengerGroupPopOut compulsory;
	protected Button submitButton;
	
	public MessengerGroupForm() 
	{
    }

    public MessengerGroupForm(String name) 
    {
        super(name);
    }
    
    public void init() 
    {
    	Application app = Application.getInstance();
    	setMethod("GET"); 
    	compulsory = new MessengerGroupPopOut("compulsory");
    	addChild(compulsory);
        compulsory.init();
        submitButton = new Button ("create",app.getMessage("com.tms.messenger.form.createButton"));
        addChild(submitButton);
    }
	
    public void onRequest(Event event) 
    {
    	init();
    }

	public MessengerGroupPopOut getCompulsory() {
		return compulsory;
	}

	public void setCompulsory(MessengerGroupPopOut compulsory) {
		this.compulsory = compulsory;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}
    
	public Forward onValidate(Event event)
	{
		String[] userIds = compulsory.getIds();
		if (userIds != null && userIds.length != 0)
		{
			String[] noUserIds = new String[userIds.length];
			for(int i = 0, j=0; i < userIds.length; i++)
			{
				if (!userIds[i].equals(getWidgetManager().getUser().getId()) && !userIds[i].equals(null))
				{
					noUserIds[j] = userIds[i];
					j++;
				}
			}
			MessageModule module = (MessageModule) Application.getInstance().getModule(MessageModule.class);		
			module.initGroupChat(noUserIds, getWidgetManager().getUser().getId());
			compulsory.setSelectedOptions(new String[] {});
			return new Forward("forwardSuccess");
		}
		return null;
	}
	
	public String getDefaultTemplate() {
		return "messenger/messengerGroupForm";
	}
}