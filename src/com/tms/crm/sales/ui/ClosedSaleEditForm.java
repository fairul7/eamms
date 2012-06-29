package com.tms.crm.sales.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.stdui.Button;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.misc.DateUtil;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 24, 2004
 * Time: 3:38:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClosedSaleEditForm extends CloseSaleForm{

    protected Button cancel;
    public static final String FORWARD_CANCEL = "cancel";

    /* Step 1: Initialization */
    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
        addChild(cancel);
    }

    public void onRequest(Event evt) {
        if(getOpportunityID()!=null&&getOpportunityID().trim().length()>0){
            populateForm();
        }
//        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void populateForm(){
        OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
        Opportunity op = om.getOpportunity(getOpportunityID());
        df_OpportunityEnd.setDate(op.getOpportunityEnd());
        //DecimalFormat format =      (DecimalFormat) DecimalFormat.getInstance();
        tf_OpportunityValue.setValue(DecimalFormat.getInstance().format(op.getOpportunityValue()));
        tf_CloseReferenceNo.setValue(op.getCloseReferenceNo());
        tb_OpportunityLastRemarks.setValue(op.getOpportunityLastRemarks());
    }

    public Forward onValidate(Event evt) {

        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            String userId            = getWidgetManager().getUser().getId();

            Opportunity opp = module.getOpportunity(getOpportunityID());
            opp.setOpportunityStatus(Opportunity.STATUS_CLOSE);
            opp.setOpportunityEnd(df_OpportunityEnd.getDate());
            opp.setOpportunityValue(Double.parseDouble((String) tf_OpportunityValue.getValue()));
            opp.setCloseReferenceNo((String) tf_CloseReferenceNo.getValue());
            opp.setOpportunityLastRemarks((String) tb_OpportunityLastRemarks.getValue());
            opp.setModifiedDate(DateUtil.getToday());
            opp.setModifiedBy(userId);
            module.updateOpportunity(opp);

            return new Forward("opportunityUpdated");

        }else if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
            return new Forward(FORWARD_CANCEL);
        }
        return null;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }
}
