package com.tms.crm.sales.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.crm.sales.model.LeadModule;
import com.tms.crm.sales.model.Lead;
import com.tms.crm.sales.model.LeadException;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 22, 2004
 * Time: 3:13:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadEditForm extends LeadForm{
    private String id = null;
    public static final String FORWARD_UPDATED="updated";


    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.

        if(id!=null&&id.trim().length()>0){
            populateForm();
        }
    }


    private void populateForm(){
        LeadModule lm= (LeadModule) Application.getInstance().getModule(LeadModule.class);
        try {
            Lead lead = lm.getLead(id);
            if(lead.getCompanyId()!=null&&lead.getCompanyId().trim().length()>0){
                sel_Companies.setSelectedOption(lead.getCompanyId());
            }else{
                newCompany.setValue(lead.getCompanyName());
            }
            contactNo.setValue(lead.getTel());
            contactName.setValue(lead.getContactName());
            remarks.setValue(lead.getRemarks());
            //sel_OpportunitySource.setSelectedOption(lead.getSource());
            sel_OpportunitySource.setSelectedOptions(new String[]{lead.getSource()});
        } catch (LeadException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public Forward onValidate(Event evt) {
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            Lead lead = assembleLead();
            lead.setId(id);
            lead.setModifiedBy(getWidgetManager().getUser().getId());
            LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
            try {
                lm.updateLead(lead);
                init();
                return new Forward(FORWARD_UPDATED);
            } catch (LeadException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
            }
        }

        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public boolean isEditMode() {
		return true;
	}
}
