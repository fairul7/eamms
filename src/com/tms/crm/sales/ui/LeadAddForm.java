package com.tms.crm.sales.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.crm.sales.model.Lead;
import com.tms.crm.sales.model.LeadModule;
import com.tms.crm.sales.model.LeadException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 22, 2004
 * Time: 1:43:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadAddForm extends LeadForm{

    public static final String FORWARD_ADDED = "added";

    public Forward onValidate(Event evt) {
       if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
           Lead lead = assembleLead();
           LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
           try {
               lm.addLead(lead);
               init();
               return new Forward(FORWARD_ADDED);
           } catch (LeadException e) {
               Log.getLog(getClass()).error(e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
           }
       }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }


}
