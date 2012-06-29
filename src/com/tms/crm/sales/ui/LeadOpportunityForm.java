package com.tms.crm.sales.ui;

import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.crm.sales.model.LeadModule;
import com.tms.crm.sales.model.Lead;
import com.tms.crm.sales.model.LeadException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 23, 2004
 * Time: 5:12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadOpportunityForm extends OpportunityForm {

    private String leadId  = null;

    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        if(leadId!=null&&leadId.trim().length()>0){
            populateForm();
        }
    }

    private void populateForm(){
        LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
        try {
            Lead lead = lm.getLead(leadId);
            if(lead!=null){
                tf_OpportunityName.setValue(lead.getRemarks());
                sel_OpportunitySource.setSelectedOption(lead.getSource());
                tb_OpportunityLastRemarks.setValue(lead.getRemarks());
            }
        } catch (LeadException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }


    }


    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }


}
