/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.*;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;

import kacang.Application;

import kacang.stdui.*;

import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.lang.NumberUtils;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.NoSuchElementException;
import java.text.DecimalFormat;


public class ClaimFormItemTravelForm extends Form {
    protected DateField df_TimeFrom;

    //    protected DateField df_TimeTo;
    //   protected DateField df_TimeFinancial;
    protected SelectBox sb_Category;
    protected SelectBox sb_StandardType;
    protected SelectBox sb_ProjectId;
    protected SelectBox sb_Currency;
    protected TextField tf_Mileage;
    protected TextField tf_Toll;
    protected TextField tf_Parking;

    //add in for new travel attributes
    protected TextField tf_travelTo;
    protected TextField tf_travelFrom;
    protected TextField tf_Allowance;
    protected TextField tf_Description;
    protected TextField tf_Remarks;
    protected TextField tf_RejectReason;
    protected SelectBox sb_State;
    protected SelectBox sb_Status;
    protected Button bn_Submit;
    protected String formId;

    protected String claimant;


    public String getDefaultTemplate() {
    	return "claims/claimFormItemTravelForm";
    }
    
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

    public void init() {
        Application application = Application.getInstance();


        setColumns(2);


        addChild(new Label("date", "<b>" + Application.getInstance().getMessage("claims.label.date","Date")+" *" + "</b>"));
        df_TimeFrom = new DatePopupField("df_TimeFrom");
        addChild(df_TimeFrom);

        addChild(new Label("Remarks", "<b>" + Application.getInstance().getMessage("claims.label.purpose","Purpose") +" *"+ "</b>"));
        tf_Remarks = new TextField("tf_Remarks");
        tf_Remarks.setSize("50");
        tf_Remarks.setMaxlength("50");

        Validator vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("claims.message.mustNotBeEmpty","Must not be empty"));
        tf_Remarks.addChild(vne);
        addChild(tf_Remarks);

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
        sb_ProjectId.setSelectedOption("");
        addChild(sb_ProjectId);

        addChild(new Label("travelFrom", "<B>" + Application.getInstance().getMessage("claims.label.from","From")+" *" + "</b>"));
        tf_travelFrom = new TextField("tf_travelFrom");
        tf_travelFrom.setSize("20");
        tf_travelFrom.setMaxlength("20");
        tf_travelFrom.addChild(new ValidatorNotEmpty("from", Application.getInstance().getMessage("claims.message.mustNotBeEmpty","Must not be empty")));
        addChild(tf_travelFrom);
        addChild(new Label("travelTo", "<B>" + Application.getInstance().getMessage("claims.label.to","To") +" *"+ "</b>"));
        tf_travelTo = new TextField("tf_travelTo");
        tf_travelTo.setSize("20");
        tf_travelTo.setMaxlength("20");
        tf_travelTo.addChild(new ValidatorNotEmpty("to", Application.getInstance().getMessage("claims.message.mustNotBeEmpty","Must not be empty")));
        addChild(tf_travelTo);

        ValidatorIsNumeric vnu = new ValidatorIsNumeric("vnu",
        		Application.getInstance().getMessage("claims.message.mustBeNumber","Must be a number!"));
        addChild(new Label("mileage", "<b>" + Application.getInstance().getMessage("claims.label.mileageKm","Mileage (km)") + "</b>"));
        tf_Mileage = new TextField("tf_Mileage");
        tf_Mileage.addChild(vnu);
        tf_Mileage.setSize("5");
        tf_Mileage.setMaxlength("5");
        tf_Mileage.setValue("0");
        addChild(tf_Mileage);

        addChild(new Label("toll", "<b>" + Application.getInstance().getMessage("claims.label.toll","Toll ($)") + "</b>"));
        tf_Toll = new TextField("tf_Toll");
        tf_Toll.addChild(vnu);
        tf_Toll.setSize("9");
        tf_Toll.setMaxlength("9");
        tf_Toll.setValue("0.00");
        addChild(tf_Toll);

        addChild(new Label("parking", "<b>" + Application.getInstance().getMessage("claims.label.parking","Parking ($)") + "</b>"));
        tf_Parking = new TextField("tf_Parking");
        tf_Parking.addChild(vnu);
        tf_Parking.setSize("9");
        tf_Parking.setMaxlength("9");
        tf_Parking.setValue("0.00");
        addChild(tf_Parking);

        addChild(new Label("allowance",
                "<b>" + Application.getInstance().getMessage("claims.label.allowanceAndOther","Allowance and other expenses") + "</b>"));
        tf_Allowance = new TextField("tf_Allowance");
        tf_Allowance.addChild(vnu);
        tf_Allowance.setSize("9");
        tf_Allowance.setMaxlength("9");
        tf_Allowance.setValue("0.00");
        addChild(tf_Allowance);

        addChild(new Label("a", ""));
        addChild(new Label("b", ""));
        addChild(new Label("c", ""));
        addChild(new Label("d", ""));
        addChild(new Label("e", ""));

        bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.label.addItem","Add Item"));
        addChild(bn_Submit);
    }



    public Forward onValidate(Event evt) {

        super.onValidate(evt);
        Application application = Application.getInstance();
        ClaimFormItemModule module = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
        ClaimFormIndexModule moduleForm = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);
        ClaimFormIndex formObj = moduleForm.selectObject(formId);
        ClaimConfigTypeModule configModule = (ClaimConfigTypeModule) application.getModule(ClaimConfigTypeModule.class);

        String stdTypeId = (String) ClaimStandardTypeModule.DEFAULTID; // sb_StandardType.getSelectedOptions().keySet().iterator().next();
        String projectId = (String) sb_ProjectId.getSelectedOptions().keySet()
                                                .iterator().next();
        BigDecimal dollarPerKM = ClaimConfigMileage.getDollarPerKM(application);
       // BigDecimal theMileage = new BigDecimal((String) tf_Mileage.getValue());
        BigDecimal theMileage =null;
        try{
          
        	theMileage = new BigDecimal( new DecimalFormat("0.00").format(Integer.parseInt((String)tf_Mileage.getValue())));
          
          
        }catch(Exception e){
          theMileage= new BigDecimal(0);
          tf_Mileage.setInvalid(true);
             setInvalid(true);
             return new Forward("OnlyAcceptInt");

        }

            // BigDecimal theToll = new BigDecimal((String) tf_Toll.getValue());

           BigDecimal theToll = null;
        try{ theToll =new BigDecimal(new DecimalFormat("0.00").format(Double.parseDouble((String) tf_Toll.getValue())));
        }
        catch(Exception e){
            theToll= new BigDecimal(0);
           setInvalid(true);

        }
        BigDecimal theParking = null;
        try{
        theParking =new BigDecimal(new DecimalFormat("0.00").format(Double.parseDouble((String) tf_Parking.getValue())));
        }
        catch(Exception e){
            theParking = new BigDecimal(0);
             setInvalid(true);

        }
            BigDecimal theAllowance = null;
        try{
        theAllowance=new BigDecimal(new DecimalFormat("0.00").format(Double.parseDouble((String) tf_Allowance.getValue())));
        }
        catch(Exception e){
            theAllowance = new BigDecimal(0);
             setInvalid(true);

        }


        //check what that person belong to
        String userId="";
        if(getClaimant()!=null && !("".equals(getClaimant())))
        userId = getClaimant();
        else
        userId = application.getCurrentUser().getId();

        String department = "";
        EmployeeModule employeeModule = (EmployeeModule) Application.getInstance()
                                                                    .getModule(EmployeeModule.class);

        try {
            DepartmentDataObject object = employeeModule.getEmployeeDepartment(userId);
            department = object.getDeptCode();
        } catch (EmployeeException e) {
            Log.getLog(getClass()).warn("Cannot get this person from department ");
        }


        String type = "";

        if (!(department.equals(""))) {
            Collection typeDepartCol = configModule.retrieveAllTypeDepartment();

            if ((typeDepartCol != null) && (typeDepartCol.size() > 0)) {
                for (Iterator icount = typeDepartCol.iterator();
                     icount.hasNext();) {
                    ClaimTypeDepartObject object = (ClaimTypeDepartObject) icount.next();

                    if (object.getDepartmentid().equals(department)) {
                        type = object.getTypeid();
                    }
                }
            }
        }

        String typeName = "";

        if (!(type.equals("")) && (type != null)) {

            ClaimTypeObject object = null;
            try{
            if(configModule.selectTypeName(type) !=null)
             object= configModule.selectTypeName(type);


            if (object.getTypeName() != null) {
                typeName = object.getId();
                stdTypeId = typeName;
            } else {
                typeName = "";
            }

            }
            catch(NoSuchElementException e){
            //ignore coz sometime department is not assigned to type
                typeName="";
                return new Forward("notype");
            }

        }

        /// first, create the Mileage claim

        if (theMileage.compareTo(new BigDecimal("0.00")) > 0) {
            ClaimFormItem obj = new ClaimFormItem();
            UuidGenerator uuid = UuidGenerator.getInstance();
            obj.setId(uuid.getUuid());
            obj.setFormId(formObj.getId());
            obj.setTimeFrom(df_TimeFrom.getDate());
            obj.setTimeTo(df_TimeFrom.getDate());
            obj.setTimeFinancial(df_TimeFrom.getDate());

            obj.setCurrency(formObj.getCurrency());
            obj.setAmount(theMileage.multiply(dollarPerKM));
            obj.setUnitPrice(dollarPerKM);
            obj.setRemarks((String) tf_Remarks.getValue());
            obj.setQty(theMileage);
            obj.setCategoryId(ClaimFormItemCategoryModule.TRAVEL_MILEAGE);
            obj.setStandardTypeId(stdTypeId);
            obj.setProjectId(projectId);
            obj.setState("pap");
            obj.setStatus("act");

            //add for new travel attributes
            obj.setTravelFrom((String) tf_travelFrom.getValue());
            obj.setTravelTo((String) tf_travelTo.getValue());

            //module.addObject(obj);
            if(module.checkSameDayDiffName(df_TimeFrom.getDate(), (String)tf_Remarks.getValue(), obj.getCategoryId()) == true)
            return new Forward("sameNameExist");

            if(!module.addObject(obj)) return new Forward("fail");
        }

        /// first, create the Toll claim
        if (theToll.compareTo(new BigDecimal("0.00")) > 0) {
            ClaimFormItem obj = new ClaimFormItem();
            UuidGenerator uuid = UuidGenerator.getInstance();
            obj.setId(uuid.getUuid());
            obj.setFormId(formObj.getId());
            obj.setTimeFrom(df_TimeFrom.getDate());
            obj.setTimeTo(df_TimeFrom.getDate());
            obj.setTimeFinancial(df_TimeFrom.getDate());

            obj.setCurrency(formObj.getCurrency());
            obj.setAmount(theToll);
            obj.setUnitPrice(theToll);
            obj.setRemarks((String) tf_Remarks.getValue());
            obj.setQty(new BigDecimal("1.00"));
            obj.setCategoryId(ClaimFormItemCategoryModule.TRAVEL_TOLL);
            obj.setStandardTypeId(stdTypeId);
            obj.setProjectId(projectId);
            obj.setState("pap");
            obj.setStatus("act");

             if(module.checkSameDayDiffName(df_TimeFrom.getDate(), (String)tf_Remarks.getValue(), obj.getCategoryId()) == true)
            return new Forward("sameNameExist");

            //module.addObject(obj);
            if(!module.addObject(obj)) return new Forward("fail");
        }

        /// lastly, create the Parking claim
        if (theParking.compareTo(new BigDecimal("0.00")) > 0) {
            ClaimFormItem obj = new ClaimFormItem();
            UuidGenerator uuid = UuidGenerator.getInstance();
            obj.setId(uuid.getUuid());
            obj.setFormId(formObj.getId());
            obj.setTimeFrom(df_TimeFrom.getDate());
            obj.setTimeTo(df_TimeFrom.getDate());
            obj.setTimeFinancial(df_TimeFrom.getDate());

            obj.setCurrency(formObj.getCurrency());
            obj.setAmount(theParking);
            obj.setUnitPrice(theParking);
            obj.setRemarks((String) tf_Remarks.getValue());
            obj.setQty(new BigDecimal("1.00"));
            obj.setCategoryId(ClaimFormItemCategoryModule.TRAVEL_PARKING);
            obj.setStandardTypeId(stdTypeId);
            obj.setProjectId(projectId);
            obj.setState("pap");
            obj.setStatus("act");

            if(module.checkSameDayDiffName(df_TimeFrom.getDate(), (String)tf_Remarks.getValue(), obj.getCategoryId()) == true)
            return new Forward("sameNameExist");
            //module.addObject(obj);
            if(!module.addObject(obj)) return new Forward("fail");
        }

        if (theAllowance.compareTo(new BigDecimal("0.00")) > 0) {
            ClaimFormItem obj = new ClaimFormItem();
            UuidGenerator uuid = UuidGenerator.getInstance();
            obj.setId(uuid.getUuid());
            obj.setFormId(formObj.getId());
            obj.setTimeFrom(df_TimeFrom.getDate());
            obj.setTimeTo(df_TimeFrom.getDate());
            obj.setTimeFinancial(df_TimeFrom.getDate());

            obj.setCurrency(formObj.getCurrency());
            obj.setAmount(theAllowance);
            obj.setUnitPrice(theAllowance);
            obj.setRemarks((String) tf_Remarks.getValue());
            obj.setQty(new BigDecimal("1.00"));
            obj.setCategoryId(ClaimFormItemCategoryModule.TRAVEL_ALLOWANCE);
            obj.setStandardTypeId(stdTypeId);
            obj.setProjectId(projectId);
            obj.setState("pap");
            obj.setStatus("act");

            if(module.checkSameDayDiffName(df_TimeFrom.getDate(), (String)tf_Remarks.getValue(), obj.getCategoryId()) == true)
            return new Forward("sameNameExist");

            //module.addObject(obj);
            if(!module.addObject(obj)) return new Forward("fail");
        }

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


    public String getClaimant() {
        return claimant;
    }

    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

}
