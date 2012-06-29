/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.crm.sales.misc.MyUtil;
import com.tms.hr.claim.model.ClaimFormItemCategory;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimStandardType;
import com.tms.hr.claim.model.ClaimStandardTypeModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public class ClaimStandardTypeForm extends Form 
{
//   protected TextField tf_Category;
//   protected TextField tf_Code;
   protected TextField tf_Name;
   protected SelectBox sb_Category;

   protected TextField tf_Description;
   protected TextField tf_Amount;
//   protected TextField tf_UserEdit;
   protected SelectBox sb_State;
   protected TextField tf_Status;
   protected Button submit;

//   private Label lbCategory;
//   private Label lbCode;
   private Label lbName;
   private Label lbDescription;
   private Label lbCurrency;
   private Label lbAmount;
   private Label lbUserEdit;
   private Label lbTimeEdit;
   private Label lbState;
   private Label lbStatus;

   private String claimStandardTypeID ;

	private String type; // possible values: "View", "Add", "Edit"
	
	
	/* Step 1: Initialization */
	public void init() 
	{
        setMethod("POST");
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"}))
		{
			type = "Add";
			Log.getLog(this.getClass()).debug("Error!!! Wrong type passed. ClaimStandardTypeForm");
		}
	}
	
	public String getType() { return type; }
	public void setType(String string) { type = string; }
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getClaimStandardTypeID() {
		return claimStandardTypeID;
	}
	
	public void setClaimStandardTypeID(String string) {
		claimStandardTypeID = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() 
	{
		removeChildren();
		setColumns(2);
		setMethod("GET");

		addChild(new Label("lb3", "<b>"+"Standard Name "+"</b>"));
		if (type.equals("View")) {
			lbName = new Label("lbName", "");
			addChild(lbName);
		} else {
			tf_Name = new TextField("tf_Name");
			tf_Name.setMaxlength("255");
			tf_Name.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
			tf_Name.addChild(vne);
			addChild(tf_Name);
		}

      addChild(new Label("category","<b>"+"Category"+"</b>"));
      sb_Category = new SelectBox("sb_Category");
      /// need to have a loop here to populate all active category
//    sb_Category;
      {
		 Application application = Application.getInstance();
         ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule)
               application.getModule(ClaimFormItemCategoryModule.class);
         Vector colCat = new Vector( module.selectObjects("status","act", 0, -1));
         for(int cnt1 = 0; cnt1<colCat.size(); cnt1++)
         {
            ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);
            sb_Category.addOption(valObj.getId(),valObj.getName());
         }
      }
//      sb_Category.setSelectedOptions(new String[]
 //                       { ClaimFormItemCategoryModule.DEFAULTID});
      addChild(sb_Category);

/*
      addChild(new Label("lb1", "Category: "));
      if (type.equals("View")) {
         lbCategory = new Label("lbCategory", "");
         addChild(lbCategory);
      } else {
         tf_Category = new TextField("tf_Category");
         tf_Category.setMaxlength("255");
         tf_Category.setSize("40");
         ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
         tf_Category.addChild(vne);
         addChild(tf_Category);
      }
	
      addChild(new Label("lb2", "Standard Type: "));
      if (type.equals("View")) {
         lbCode = new Label("lbCode", "");
         addChild(lbCode);
      } else {
         tf_Code = new TextField("tf_Code");
         tf_Code.setMaxlength("255");
         tf_Code.setSize("40");
         ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
         tf_Code.addChild(vne);
         addChild(tf_Code);
      }
*/

     addChild(new Label("lb4","<b>"+"Description "+"</b>"));
      if (type.equals("View")) {
         lbDescription = new Label("lbDescription", "");
         addChild(lbDescription);
      } else {
         tf_Description = new TextField("tf_Description");
         tf_Description.setMaxlength("255");
         tf_Description.setSize("40");
         ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
         tf_Description.addChild(vne);
         addChild(tf_Description);
      }
/*
     addChild(new Label("lb5", "Currency: "));
      if (type.equals("View")) {
         lbCurrency = new Label("lbCurrency", "");
         addChild(lbCurrency);
      } else {
         sb_Currency = new SelectBox("sb_Currency");
			sb_Currency.addOption("MYR","MYR");
			sb_Currency.addOption("SGD","SGD");
			sb_Currency.addOption("THB","THB");
//         sb_Currency.setMaxlength("255");
 //        sb_Currency.setSize("40");
//         ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
 //        sb_Currency.addChild(vne);
			addChild(sb_Currency);
      }
*/

		addChild(new Label("lb6","<b>"+"Amount"+"</b>"));
		if(type.equals("View")){
			lbAmount = new Label("lbAmount");
			addChild(lbAmount);
		} else {
			tf_Amount = new TextField("tf_Amount");
			tf_Amount.setMaxlength("12");
			tf_Amount.setSize("12");
			ValidatorIsNumeric vne = new ValidatorIsNumeric("vnu","Must be a number!");
			tf_Amount.addChild(vne);
			addChild(tf_Amount);
		}
/*
		addChild(new Label("lb7","Date"));
		if(type.equals("View")){
			lbTimeEdit  = new Label("lbTimeEdit");
			addChild(lbTimeEdit);
		} else {
			df_TimeEdit = new DateField("df_TimeEdit");
			addChild(df_TimeEdit);
		}
*/		
		if (!type.equals("View")) {
            addChild(new Label("20",""));
			submit = new Button("submit", "Submit");
			addChild(submit);
		}
	}
	
	public void onRequest(Event evt) 
	{
		initForm();
/*		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Edit")) {
			populateEdit();
		}
*/
	}
	
	public Forward onValidate(Event evt) {
		Forward myForward = null;
//		if (type.equals("Add")) {
			myForward = addClaimStandardType();
//		} else if (type.equals("Edit")) {
//			myForward = editClaimStandardType();
//		}
		initForm();
		return myForward;
	}
	
	private Forward addClaimStandardType() 
	{
		Application application  = Application.getInstance();
		ClaimStandardTypeModule module = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
		
		ClaimStandardType ct = new ClaimStandardType();
		UuidGenerator uuid = UuidGenerator.getInstance();
		claimStandardTypeID = uuid.getUuid();
		ct.setClaimStandardTypeID(claimStandardTypeID);
      ct.setCategory((String) sb_Category.getSelectedOptions().keySet().iterator().next());
//		ct.setCategory((String) tf_Category.getValue());
//		ct.setCode((String) tf_Code.getValue());
		ct.setName((String) tf_Name.getValue());
		ct.setDescription((String) tf_Description.getValue());
		ct.setCurrency("MYR");
		ct.setAmount(new BigDecimal(tf_Amount.getValue().toString()));
		ct.setTimeEdit(new Date());

/*		if (!module.isUnique(ct)) {
			return new Forward("claimStandardTypeDuplicate");
		}
*/		
		module.addObject(ct);
		
		return new Forward("claimStandardTypeAdded");
	}
	
//	private Forward editClaimStandardType() 
//	{
/*		Application application  = Application.getInstance();
		ClaimStandardTypeModule module = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
		
		ClaimStandardType ct = module.getObject(claimStandardTypeID);
		ct.setCategory((String) tf_Category.getValue());
		ct.setCode((String) tf_Code.getValue());
		ct.setName((String) tf_Name.getValue());
		ct.setDescription((String) tf_Description.getValue());
		ct.setCurrency((String) sb_Currency.getValue());
		ct.setAmount(new BigDecimal(tf_Amount.getValue().toString() ));
		ct.setTimeEdit((java.util.Date)df_TimeEdit.getValue());
*/
/*	
		if (!module.isUnique(ct)) {
			return new Forward("claimStandardTypeDuplicate");
		}
*/		
//		module.updateObject(ct);
		
//		return new Forward("standardTypeEdit");
//	}
	
	public void populateView() {
		Application application  = Application.getInstance();
		ClaimStandardTypeModule module = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
		ClaimStandardType ct = module.getObject(claimStandardTypeID);
		
//		lbCategory.setText(ct.getCategory());
//		lbCode.setText(ct.getCode());
		lbName.setText(ct.getName());
		lbDescription.setText(ct.getDescription());
		lbCurrency.setText(ct.getCurrency());
		lbAmount.setText(ct.getAmount().toString());
		lbTimeEdit.setText(ct.getTimeEdit().toString());
		lbState.setText(ct.getState());
	}
	
/*	
	public String getDefaultTemplate() {
		return "claim/ClaimStandardType_Form";
	}
*/
}
