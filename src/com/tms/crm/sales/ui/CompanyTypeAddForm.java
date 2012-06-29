package com.tms.crm.sales.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.Application;
import com.tms.crm.sales.model.CompanyType;
import com.tms.crm.sales.model.CompanyTypeModule;
import com.tms.crm.sales.model.CompanyTypeException;
import com.tms.crm.sales.misc.MyUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 4:07:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeAddForm extends CompanyTypeForm{

    public static final String FORWARD_COMPANYTYPE_ADDED ="typeadded";
    public static final String FORWARD_DUPLICATED="duplicated";

    public CompanyTypeAddForm() {
    }

    public CompanyTypeAddForm(String name) {
        super(name);
    }

    
    public Forward onValidate(Event evt) {
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            CompanyType companyType = assembleCompanyType();
            Forward forward;
            CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
            try {
                if(!ctm.isUnique(companyType))
                    forward = new Forward(FORWARD_DUPLICATED);
                else{
                    ctm.addCompanyType(companyType);
                    forward = new Forward(FORWARD_COMPANYTYPE_ADDED);
                    init();
                }

                return forward;
            } catch (CompanyTypeException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private CompanyType assembleCompanyType(){
        CompanyType companyType = new CompanyType();
        companyType.setId(UuidGenerator.getInstance().getUuid());
        companyType.setType(tf_type.getValue().toString());
        companyType.setArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived).equals("0")?false:true);
        return companyType;
    }



}
