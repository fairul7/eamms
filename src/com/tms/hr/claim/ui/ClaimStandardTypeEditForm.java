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
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.math.BigDecimal;
import java.util.Vector;
import java.text.Format;
import java.text.NumberFormat;



public class ClaimStandardTypeEditForm extends Form
{

//   protected TextField tf_Category;
//   protected TextField tf_Code;
   protected TextField tf_Name;
	protected SelectBox sb_Category;
   protected TextField tf_Description;
//   protected SelectBox sb_Currency;
   protected TextField tf_Amount;
//   protected TextField tf_UserEdit;
//   protected DateField df_TimeEdit;
   protected SelectBox sb_State;
   protected TextField tf_Status;
   protected Button submit;


	private String claimStandardTypeID ;
	private boolean editStatusUseLabel;

	private String type ; // possible values: "View", "Add", "Edit"


	/// step 1: Initialization
	public void init()
	{
        setMethod("POST");
		if(!MyUtil.isValidChoice(type, new String[] {"View", "Edit"}))
		{ type = "Edit";
			Log.getLog(this.getClass()).debug("Error!!! Wrong type passed. ClaimStandardTypeEditForm");
		}
		populateMember();
	}

	public String getType() { return type;}
	public void setType(String theType) { this.type = theType;}

	/// step 2: Parameter passing (dynamic) */

	public boolean getEditStatusUseLabel()
	{ return this.editStatusUseLabel;}

	public void setClaimStandardTypeID(String id)
	{ 
		this.claimStandardTypeID = id;
		populateEdit();
	}
	public String getClaimStandardTypeID()
	{ return this.claimStandardTypeID;}

	public void setId(String id)
	{ this.claimStandardTypeID = id;}
	public String getId()
	{ return this.claimStandardTypeID;}

	public void initForm()
	{
		removeChildren();
		setColumns(2);
		setMethod("GET");
/*
      addChild(new Label("lb1", "Category: "));
      addChild(tf_Category);

      addChild(new Label("lb2", "Standard Type: "));
         addChild(tf_Code);
*/

		addChild(new Label("lb3", "<b>"+"Standard Name "+"</b>"));
		addChild(tf_Name);

      addChild(new Label("category","<b>"+"Category"+"</b>"));
		addChild(sb_Category);
/*      sb_Category = new SelectBox("sb_Category");
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
 */




     addChild(new Label("lb4", "<b>"+"Description "+"</b>"));
         addChild(tf_Description);

//     addChild(new Label("lb5", "Currency: "));
//			addChild(sb_Currency);

		addChild(new Label("lb6","<b>"+"Amount"+"</b>"));
			addChild(tf_Amount);

//		addChild(new Label("lb7","User"));
//			addChild(tf_UserEdit);

//		addChild(new Label("lb8","Date"));
//			addChild(df_TimeEdit);

         addChild(new Label("20",""));
         submit = new Button("submit", "Submit");
         addChild(submit);


	} /// end of initForm


	public Forward onValidate(Event evt)
	{
		Forward myFwd = null;
//		if(type.equals("Add"))
//		{ myFwd = addObject();}
//		else if(type.equals("Edit"))
		if(type.equals("Edit"))
		{ myFwd = editObject();}
		myFwd = new Forward("claimStandardTypeEdit");
		initForm();
		return myFwd;
	}

	private Forward editObject()
	{
		Application application = Application.getInstance();
		ClaimStandardTypeModule module = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
		String userId = getWidgetManager().getUser().getId();
		Log.getLog(this.getClass()).debug("ClaimStandardTypeID==="+claimStandardTypeID);
		ClaimStandardType obj = module.getObject(claimStandardTypeID);
		obj.setCategory((String) sb_Category.getSelectedOptions().keySet().iterator().next());
//		obj.setCode((String) tf_Code.getValue());
		obj.setName((String) tf_Name.getValue());
		obj.setDescription((String) tf_Description.getValue());
//		obj.setCurrency((String) sb_Currency.getSelectedOptions().keySet().iterator().next());
		obj.setAmount(new BigDecimal((String)tf_Amount.getValue().toString()));
		obj.setUserEdit(userId);
 //     obj.setTimeEdit(df_TimeEdit.getDate());

	//	obj.setState((String) tf_State.getValue()); ????
		obj.setStatus((String) tf_Status.getValue());
	
		module.updateObject(obj);
		return new Forward("claimStandardTypeUpdated");
	

	}

	public void populateEdit()
	{
		Application application = Application.getInstance();
		ClaimStandardTypeModule module = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);	
		ClaimStandardType obj = module.getObject(claimStandardTypeID);

		sb_Category.setSelectedOptions(new String[]{obj.getCategory()});
//		tf_Category.setValue(obj.getCategory());
//		tf_Code.setValue(obj.getCode());
		tf_Description.setValue(obj.getDescription());
		tf_Name.setValue(obj.getName());
//		sb_Currency.setSelectedOptions(new String[]{obj.getCurrency()});

		tf_Amount.setValue(NumberFormat.getInstance().format(obj.getAmount()));
//		tf_UserEdit.setValue(obj.getUserEdit());
//		df_TimeEdit.setDate(obj.getTimeEdit());
		sb_State.setSelectedOptions(new String[]{obj.getState()});
		tf_Status.setValue(obj.getStatus());
	}


	public void populateMember()
	{
/*      tf_Category = new TextField("tf_Category");
      tf_Category.setMaxlength("255");
      tf_Category.setSize("40");
      ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
      tf_Category.addChild(vne);

      tf_Code = new TextField("tf_Code");
      tf_Code.setMaxlength("255");
      tf_Code.setSize("40");
      vne = new ValidatorNotEmpty("vne", "Must not be empty");
      tf_Code.addChild(vne);
*/
      ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
		tf_Name = new TextField("tf_Name");
		tf_Name.setMaxlength("255");
		tf_Name.setSize("40");
		tf_Name.addChild(vne);
		vne = new ValidatorNotEmpty("vne", "Must not be empty");
		tf_Name.addChild(vne);

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

      tf_Description = new TextField("tf_Description");
      tf_Description.setMaxlength("255");
      tf_Description.setSize("40");
      vne = new ValidatorNotEmpty("vne", "Must not be empty");
      tf_Description.addChild(vne);
/*
      sb_Currency = new SelectBox("sb_Currency");
		sb_Currency.addOption("MYR","MYR");
		sb_Currency.addOption("SGD","SGD");
		sb_Currency.addOption("THB","THB");
*/
		tf_Amount = new TextField("tf_Amount");
		tf_Amount.setMaxlength("12");
		tf_Amount.setSize("12");
		vne = new ValidatorNotEmpty("vne","Must not be empty");
		tf_Amount.addChild(vne);
//		ValidatorIsNumeric vn = new ValidatorIsNumeric("vne","Must be a number");
//		tf_Amount.addChild(vn);

//		tf_UserEdit = new TextField("tf_UserEdit");

//		df_TimeEdit = new DateField("df_TimeEdit");

		sb_State = new SelectBox("sb_State");
		sb_State.addOption("act","active");

   	tf_Status = new TextField("tf_Status");

//		df_TimeEdit = new DateField("df_TimeEdit");

	}

	public void onRequest(Event evt)
	{
		populateEdit();
		initForm();
	}

}

