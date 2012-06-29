package com.tms.collab.formwizard.ui;

import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;

import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.model.FormDocumentException;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 23, 2003
 * Time: 7:29:47 PM
 * To change this template use Options | File Templates.
 */
public class RejectForm extends Form
{
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;
    private String formUID;
    
    public RejectForm()
    {
    }

    public RejectForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setColumns(1);
        reasonTB= new TextBox("reasontb");
        reasonTB.setRows("8");
        rejectButton = new Button("rejectButton","Reject");
        cancelButton = new Button("cancelButton","Cancel");
        addChild(new Label("labelCaption", "Reason for Rejection: "));
		addChild(reasonTB);
		Panel panel = new Panel("buttonPanel");
		panel.addChild(rejectButton);
		panel.addChild(cancelButton);
        addChild(panel);
    }

    public Forward onValidate(Event event)
    {
        super.onValidate(event);
        String buttonClicked = findButtonClicked(event);
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
            FormModule module = (FormModule)Application.getInstance().getModule(FormModule.class);
            
			try {
				module.approveFormData(String.valueOf(reasonTB.getValue()),getFormUID(),"reject",event.getWidgetManager().getUser().getId(), event);
			
				return new Forward("rejected");
			}
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }            
			
			
            
            
        }
        if(cancelButton.getAbsoluteName().equals(buttonClicked)){
            return new Forward("cancel");
        }
        return null;
    }



    public TextBox getReasonTB()
    {
        return reasonTB;
    }

    public void setReasonTB(TextBox reasonTB)
    {
        this.reasonTB = reasonTB;
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

    
    
    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }
	/**
	 * @return
	 */
	public String getFormUID() {
		return formUID;
	}

	/**
	 * @param string
	 */
	public void setFormUID(String formUID) {
		this.formUID = formUID;
	}

}
