package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.crm.sales.misc.MyUtil;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.CompanyModule;
import com.tms.crm.sales.model.Lead;
import com.tms.crm.sales.model.Company;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 21, 2004
 * Time: 2:23:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadForm extends Form{
    protected TextField newCompany;
    protected TextField contactName;
    protected TextBox remarks;
    protected TextField contactNo;
    protected SelectBox sel_OpportunitySource;
    protected SelectBox sel_Companies;
    protected Button cancel,submit;

    public static final String FORWARD_CANCEL  = "cancel";
    public static final String DEFAULT_TEMPLATE = "sfa/leadform";


    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        Application application = Application.getInstance();
        OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);

        setWidth("100%");
        setMethod("POST");
        sel_OpportunitySource = new SelectBox("sel_OpportunitySource");
        MyUtil.populate_SelectBox(sel_OpportunitySource, module.getSourceCollection(), "sourceID", "sourceText");
        addChild(sel_OpportunitySource);
        sel_Companies = new SelectBox("sel_Companies");
        addChild(sel_Companies);


        newCompany = new TextField("newCompany");
        contactName = new TextField("contactName");
        contactNo = new TextField("contactNo");
        remarks = new TextBox("remarks");

        newCompany.setMaxlength("255");
        contactName.setMaxlength("255");
        contactNo.setMaxlength("255");
        cancel = new Button("cancel","Cancel");

		if (isEditMode()) {
        	submit = new Button ("update","Update");
		} else {
			submit =  new Button("submit","Submit");
		}
        addChild(newCompany);
        addChild(contactName);
        addChild(contactNo);
        addChild(remarks);
        addChild(cancel);
        addChild(submit);
        refreshCompanies();
    }

    public Forward onSubmit(Event evt) {
        Forward forward = super.onSubmit(evt);
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
            return new Forward(FORWARD_CANCEL);
        }else if( submit.getAbsoluteName().equals(findButtonClicked(evt))){
            String newCompanyName = (String) newCompany.getValue();
            if(newCompanyName==null||newCompanyName.trim().length()==0){

                Collection col  =  sel_Companies.getSelectedOptions().keySet();

                if(col==null||col.size()==0||col.iterator().next().toString().equals("-1")){
                    sel_Companies.setInvalid(true);
                    setInvalid(true);
                    return forward;
                }

            }
        }
        return forward;    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void refreshCompanies(){
        if(sel_Companies!=null){
            CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);

            TreeMap sortedMap = new TreeMap();
            Map cmap =cm.getCompanyMap();
            for (Iterator iterator = cmap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                sortedMap.put(cmap.get(key),key);
            }
            SequencedHashMap companiesMap = new SequencedHashMap();
            companiesMap.put("-1","- Please Select -");
            for (Iterator iterator = sortedMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                companiesMap.put(sortedMap.get(key),key);
            }

            sel_Companies.setOptionMap(companiesMap);
        }

    }

    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        refreshCompanies();
    }

    protected Lead assembleLead(){
        Lead lead = new Lead();
        String contact = (String)contactNo.getValue();
        String newCompanyName = (String)newCompany.getValue();
        if(newCompanyName==null||newCompanyName.trim().length()==0){
            Map option = sel_Companies.getSelectedOptions();
            lead.setCompanyId(option.keySet().iterator().next().toString());
            lead.setCompanyName(/*(String)*/option.get(option.keySet().iterator().next()).toString());
            CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
            Company company = cm.getCompany(lead.getCompanyId());
            if(contact==null ||contact.trim().length()==0)
                lead.setTel(company.getCompanyTel());
            else{
                lead.setTel(contact);
            }
        }else{
            lead.setCompanyName(newCompanyName);
            lead.setTel(contact);
        }
        lead.setContactName((String)contactName.getValue());
        lead.setRemarks((String)remarks.getValue());
        lead.setUserId(getWidgetManager().getUser().getId());
        lead.setSource(MyUtil.getSingleValue_SelectBox(sel_OpportunitySource));
        lead.setModifiedBy(getWidgetManager().getUser().getId());
        lead.setModifiedDate(new Date());
        return lead;
    }


    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }


    public TextField getNewCompany() {
        return newCompany;
    }

    public void setNewCompany(TextField newCompany) {
        this.newCompany = newCompany;
    }

    public TextField getContactName() {
        return contactName;
    }

    public void setContactName(TextField contactName) {
        this.contactName = contactName;
    }

    public TextBox getRemarks() {
        return remarks;
    }

    public void setRemarks(TextBox remarks) {
        this.remarks = remarks;
    }

    public TextField getContactNo() {
        return contactNo;
    }

    public void setContactNo(TextField contactNo) {
        this.contactNo = contactNo;
    }

    public SelectBox getSel_OpportunitySource() {
        return sel_OpportunitySource;
    }

    public void setSel_OpportunitySource(SelectBox sel_OpportunitySource) {
        this.sel_OpportunitySource = sel_OpportunitySource;
    }

    public SelectBox getSel_Companies() {
        return sel_Companies;
    }

    public void setSel_Companies(SelectBox sel_Companies) {
        this.sel_Companies = sel_Companies;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

	public boolean isEditMode() {
		return false;
	}
}
