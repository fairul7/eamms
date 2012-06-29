/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.*;

import kacang.Application;

import kacang.stdui.*;

import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.UuidGenerator;

import org.apache.commons.lang.NumberUtils;

import java.math.BigDecimal;

import java.util.NoSuchElementException;
import java.util.Vector;
import java.text.DecimalFormat;


public class ClaimFormItemGeneralForm extends Form {
    protected DateField df_TimeFrom;

    //    protected DateField df_TimeTo;
    //   protected DateField df_TimeFinancial;
    protected SelectBox sb_Category;
    protected SelectBox sb_StandardType;
    protected SelectBox sb_ProjectId;
    protected SelectBox sb_Currency;
    protected TextField tf_Amount;

    //protected TextField tf_Qty; // make it default to 1
    //protected TextField tf_UnitPrice; // make it default to 1
    //protected TextField tf_Uom; // make it default to 1
    protected TextField tf_Description;
    protected TextField tf_Remarks;
    protected TextField tf_RejectReason;
    protected SelectBox sb_State;
    protected SelectBox sb_Status;
    protected Button bn_Submit;
    protected String formId;

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormId() {
        return this.formId;
    }

    public void onRequest(Event evt) {
        init();
        setMethod("POST");
    }
    
    public String getDefaultTemplate() {
    	return "claims/claimFormItemGeneralForm";
    }

    public void init() {
        Application application = Application.getInstance();
        setColumns(2);


        addChild(new Label("date", "<b>" + Application.getInstance().getMessage("claims.label.date","Date")+" *" + "</b>"));
        df_TimeFrom = new DatePopupField("df_TimeFrom");
        addChild(df_TimeFrom);

        /*
                        addChild(new Label("date", "Date"));
                        df_TimeTo = new DateField("df_TimeTo");
                        addChild(df_TimeTo);

              addChild(new Label("date", "Date"));
              df_TimeFinancial = new DateField("df_TimeFinancial");
              addChild(df_TimeFinancial);
        */
        addChild(new Label("category", "<b>" + Application.getInstance().getMessage("claims.label.category","Category") + "</b>"));
        sb_Category = new SelectBox("sb_Category");

        /// need to have a loop here to populate all active category
        //		sb_Category;
        {
            ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);
            Vector colCat = new Vector(module.selectObjects("status", "act", 0,
                        -1));

            for (int cnt1 = 0; cnt1 < colCat.size(); cnt1++) {
                ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);

                if (!("default".equals(valObj.getId())) &&
                        !("travel-mileage".equals(valObj.getId())) &&
                        !("travel-toll".equals(valObj.getId())) &&
                        !("travel-parking".equals(valObj.getId()))) {
                    sb_Category.addOption(valObj.getId(), valObj.getName());
                }
            }
        }

        sb_Category.setSelectedOptions(new String[] {
                ClaimFormItemCategoryModule.DEFAULTID
            });
        addChild(sb_Category);

        /*
                        addChild(new Label("type","Type"));
                        sb_StandardType = new SelectBox("sb_StandardType");
                        {
                 ClaimStandardTypeModule module = (ClaimStandardTypeModule)
                       application.getModule(ClaimStandardTypeModule.class);
                 Vector colCat = new Vector(module.selectObjects("status","act", 0, -1));
                 for(int cnt1 = 0; cnt1<colCat.size(); cnt1++)
                 {
                    ClaimStandardType valObj = (ClaimStandardType) colCat.get(cnt1);
                    sb_StandardType.addOption(valObj.getId(),valObj.getName());
                 }
                        }
                        sb_StandardType.setSelectedOptions(new
                                                                        String[]{ClaimStandardTypeModule.DEFAULTID});
                        addChild(sb_StandardType);
        */

        //    protected SelectBox sb_ProjectId;

        /*
                        addChild(new Label("currency", "Currency"));
                        sb_Currency = new SelectBox("sb_Currency");
                        sb_Currency.addOption("MYR","MYR");
                        sb_Currency.addOption("USD","USD");
                        sb_Currency.addOption("SGD","SGD");
                        addChild(sb_Currency);
        */
        addChild(new Label("project", "<b>" + Application.getInstance().getMessage("claims.label.project","Project") + "</b>"));
        sb_ProjectId = new SelectBox("sb_Project");
        sb_ProjectId.addOption("", "- N/A -");

        {
            ClaimProjectModule module = (ClaimProjectModule) application.getModule(ClaimProjectModule.class);
            Vector colCat = new Vector(module.selectObjects("status", "act", 0,
                        -1));

            for (int cnt1 = 0; cnt1 < colCat.size(); cnt1++) {
                ClaimProject valObj = (ClaimProject) colCat.get(cnt1);
                sb_ProjectId.addOption(valObj.getId(), valObj.getName());
            }
        }

        sb_ProjectId.setSelectedOptions(new String[] {
                ClaimProjectModule.DEFAULTID
            });
        addChild(sb_ProjectId);
        addChild(new Label("Remarks", "<b>" + Application.getInstance().getMessage("claims.label.purpose","Purpose")+" *" + "</b>"));
        tf_Remarks = new TextField("tf_Remarks");
        tf_Remarks.setSize("50");
        tf_Remarks.setMaxlength("50");

        Validator vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("claims.message.mustNotBeEmpty","Must not be empty"));
        tf_Remarks.addChild(vne);
        addChild(tf_Remarks);

        addChild(new Label("Amount", "<b>" + Application.getInstance().getMessage("claims.label.amount","Amount")+" *" + "</b>"));
        tf_Amount = new TextField("tf_Amount");

        Validator vnu = new ValidatorIsNumeric("vnu", Application.getInstance().getMessage("claims.message.mustBeNumber","Must be a number!"));
        tf_Amount.addChild(vnu);
        tf_Amount.setSize("9");
        tf_Amount.setMaxlength("9");

        //        tf_Amount.addChild(new ItemAmountValidator("val_amount", sb_StandardType));
        addChild(tf_Amount);

        addChild(new Label("a", ""));

        bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.label.addItem","Add Item"));
        addChild(bn_Submit);
    }

    public Forward onValidate(Event evt) {

         super.onValidate(evt);
        Application application = Application.getInstance();
        ClaimFormItemModule module = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
        ClaimFormIndexModule moduleForm = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);
        ClaimFormIndex formObj = moduleForm.selectObject(formId);

        String stdTypeId = ClaimStandardTypeModule.DEFAULTID; //(String) sb_StandardType.getSelectedOptions().keySet().iterator().next();

        ClaimFormItem obj = new ClaimFormItem();
        UuidGenerator uuid = UuidGenerator.getInstance();
        obj.setId(uuid.getUuid());
        obj.setFormId(formObj.getId());
        obj.setTimeFrom(df_TimeFrom.getDate());
        obj.setTimeTo(df_TimeFrom.getDate());
        obj.setTimeFinancial(df_TimeFrom.getDate());

        //		obj.setTimeTo(df_TimeTo.getDate());
        //		obj.setTimeFinancial(df_TimeFinancial.getDate());
        
        
        

        try{
        	
        	if(Double.parseDouble(tf_Amount.getValue().toString()) == 0.00)
        	{
        		tf_Amount.setInvalid(true);
        		return null;
        	}
        	

        if (stdTypeId.equals(ClaimStandardTypeModule.DEFAULTID)) {
            obj.setCurrency(formObj.getCurrency());

            String amount = "0";
            try{
            amount =new DecimalFormat("0.00").format(Double.parseDouble((String)tf_Amount.getValue()));
            }
            catch(Exception e){
                amount ="0";
                setInvalid(true);

            }

            obj.setAmount(new BigDecimal(amount));

            obj.setUnitPrice(new BigDecimal(amount));

            obj.setRemarks((String) tf_Remarks.getValue());
        } else {
            ClaimStandardTypeModule cstModule = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
            ClaimStandardType cstObj = cstModule.selectObject(stdTypeId);
            obj.setCurrency(cstObj.getCurrency());
            obj.setAmount(cstObj.getAmount());
            obj.setUnitPrice(cstObj.getAmount());
            obj.setRemarks((String) tf_Remarks.getValue());
        }


        }
        catch(Exception e){
            setInvalid(true);
            return null;
        }



        obj.setQty(new BigDecimal("1.00"));
        try{
        
        obj.setCategoryId((String) sb_Category.getSelectedOptions().keySet()
                                              .iterator().next());
        
        }
        catch(NoSuchElementException e){
        //didnt create category first	
        return new Forward("categoryemtpy");
        }
        
        obj.setStandardTypeId(ClaimStandardTypeModule.DEFAULTID);
        obj.setProjectId((String) sb_ProjectId.getSelectedOptions().keySet()
                                              .iterator().next());

        //		obj.setDescription((String)tf_Description.getValue());
        /// can set description according to ClaimStandardType
        //		obj.setRejectReason((String)tf_RejectReason.getValue());
        obj.setState("pap");
        obj.setStatus("act");

        //		obj.setState((String) sb_State.getSelectedOptions().keySet().iterator().next());
        //		obj.setStatus((String) sb_Status.getSelectedOptions().keySet().iterator().next());

        if(module.checkSameDayDiffName(df_TimeFrom.getDate(), (String)tf_Remarks.getValue(), obj.getCategoryId()) == true)
                	return new Forward("sameNameExist");


        if(!module.addObject(obj)) return new Forward("fail");

        /// if successfully added the ClaimFormItem, must update the ClaimFormAmount
        removeChildren();
        init();

        //return super.onValidate(evt);
        return new Forward("success");
    }

    class ItemAmountValidator extends Validator {
        SelectBox sb;

        public ItemAmountValidator() {
        }

        public ItemAmountValidator(String name) {
            setName(name);
        }

        public ItemAmountValidator(String name, SelectBox sb) {
            setName(name);
            this.sb = sb;
        }

        public boolean validate(FormField formField) {
            String s;

            try {
                // might not be a good solution
                s = (String) sb.getSelectedOptions().keySet().iterator().next();
            } catch (Exception e) {
                return true;
            }

            if (ClaimStandardTypeModule.DEFAULTID.equals(s)) {
                // check the amount
                String v = (String) formField.getValue();

                if (NumberUtils.isNumber(v)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // just let it be
                return true;
            }
        }
    }
}
