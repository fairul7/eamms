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
 * Time: 3:57:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadCompanyForm extends CompanyForm {
    private String id=null;

    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
       if(id!=null&&id.trim().length()>0){
            populateForm();
       }

    }


    private void populateForm(){
        LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
        try {
            Lead lead =  lm.getLead(id);
            tf_CompanyName.setValue(lead.getCompanyName());
            tf_CompanyTel.setValue(lead.getTel());
        } catch (LeadException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
