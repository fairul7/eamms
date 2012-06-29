/*
 * Created on Feb 4, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;
import com.tms.crm.sales.ui.ValidatorIsFloat;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CloseSaleForm extends Form {

	protected DateField df_OpportunityEnd;
	protected TextField tf_OpportunityValue;
	protected TextField tf_CloseReferenceNo;
	protected TextBox   tb_OpportunityLastRemarks;
	protected Button submit,cancel;
	
	private String opportunityID;
	public static final String FORWARD_CANCEL = "cancel";
	
	/* Step 1: Initialization */
	public void init() {
		initForm();
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}


    public Forward onSubmit(Event evt) {
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
            return new Forward(FORWARD_CANCEL);
        }
        return super.onSubmit(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.dateofSale","Date of Sale")+":"));
		df_OpportunityEnd = new DateField("df_OpportunityEnd");
		addChild(df_OpportunityEnd);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.actualamount","Actual amount")+":"));
		tf_OpportunityValue = new TextField("tf_OpportunityValue");
		tf_OpportunityValue.setMaxlength("20");
		tf_OpportunityValue.setSize("20");
		ValidatorIsFloat vInt = new ValidatorIsFloat("vInt", Application.getInstance().getMessage("sfa.label.mustbeaninteger","Must be an integer"));
		tf_OpportunityValue.addChild(vInt);
		addChild(tf_OpportunityValue);
		
		addChild(new Label("lb3", Application.getInstance().getMessage("sfa.label.referenceNo","Reference No")+":"));
		tf_CloseReferenceNo = new TextField("tf_CloseReferenceNo");
		tf_CloseReferenceNo.setMaxlength("100");
		tf_CloseReferenceNo.setSize("30");
		addChild(tf_CloseReferenceNo);
		
		addChild(new Label("lb4", Application.getInstance().getMessage("sfa.label.remarks","Remarks")+":"));
		tb_OpportunityLastRemarks = new TextBox("tb_LastRemarks");
		tb_OpportunityLastRemarks.setRows("4");
		tb_OpportunityLastRemarks.setCols("40");
		addChild(tb_OpportunityLastRemarks);
		
		submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
		addChild(submit);
        cancel = new Button("cancel",Application.getInstance().getMessage("sfa.lable.cancel","Cancel"));
        addChild(cancel);
	}
	
	public void onRequest(Event evt) {
		initForm();
	}
	
	public Forward onValidate(Event evt) {
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
            return new Forward(FORWARD_CANCEL);
        }
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		String userId            = getWidgetManager().getUser().getId();
		
		Opportunity opp = module.getOpportunity(opportunityID);
		opp.setOpportunityStatus(Opportunity.STATUS_CLOSE);
		opp.setOpportunityEnd(df_OpportunityEnd.getDate());
		opp.setOpportunityValue(Double.parseDouble((String) tf_OpportunityValue.getValue()));
		opp.setCloseReferenceNo((String) tf_CloseReferenceNo.getValue());
		opp.setOpportunityLastRemarks((String) tb_OpportunityLastRemarks.getValue());
		opp.setModifiedDate(DateUtil.getToday());
		opp.setModifiedBy(userId);

        OpportunityContactModule ocm = (OpportunityContactModule) Application.getInstance().getModule(OpportunityContactModule.class);
        Collection col = ocm.listOpportunityContacts(opportunityID,OpportunityContact.COMPANY_CONTACT,null,false,0,-1);
        if(col!=null&&opp.getHasPartner().equals("1")){
            col.addAll(ocm.listOpportunityContacts(opportunityID,OpportunityContact.PARTNER_CONTACT,null,false,0,-1));
        }
        if(col!=null){
            ContactModule cm = (ContactModule) Application.getInstance().getModule(ContactModule.class);
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                OpportunityContact opportunityContact = (OpportunityContact) iterator.next();
                Contact contact = cm.getContact(opportunityContact.getContactID());
                ocm.addClosedOpportunityContact(contact);
            }
        }

		module.updateOpportunity(opp);
		
		return new Forward("opportunityUpdated");
	}
	
	public String getDefaultTemplate() {
		return "sfa/CloseSale_Form";
	}

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }
}
