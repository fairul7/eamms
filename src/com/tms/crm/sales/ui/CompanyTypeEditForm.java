package com.tms.crm.sales.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.crm.sales.model.CompanyType;
import com.tms.crm.sales.model.CompanyTypeModule;
import com.tms.crm.sales.model.CompanyTypeException;
import com.tms.crm.sales.misc.MyUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 4:40:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeEditForm extends CompanyTypeForm{
    public static final String FORWARD_DUPLICATED="duplicated";

    public static final String FORWARD_UPDATED ="updated";
    public CompanyTypeEditForm() {
    }

    public CompanyTypeEditForm(String name) {
        super(name);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        if(getId()!=null&&getId().trim().length()>0){
            populateForm();
        }
    }

    private void populateForm(){
        CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
        try {
            CompanyType companyType = ctm.getCompanyType(getId());
            tf_type.setValue(companyType.getType());
            sel_IsArchived.setSelectedOption(companyType.isArchived()?"1":"0");

        } catch (CompanyTypeException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }


    }

    public Forward onValidate(Event evt) {
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            CompanyType companyType = assembleCompanyType();
            CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
            try {
                if(!ctm.isUnique(companyType)){
                    return new Forward(FORWARD_DUPLICATED);
                }
                ctm.updateCompanyType(companyType);
                init();
                return new Forward(FORWARD_UPDATED);
            } catch (CompanyTypeException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }

        }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private CompanyType assembleCompanyType(){
        CompanyType companyType = new CompanyType();
        companyType.setId(getId());
        companyType.setType(tf_type.getValue().toString());
        companyType.setArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived).equals("0")?false:true);
        return companyType;
    }

	public boolean isEditMode() {
		return true;
	}

}
