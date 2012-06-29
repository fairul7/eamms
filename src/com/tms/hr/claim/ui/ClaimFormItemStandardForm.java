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
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.lang.NumberUtils;

import java.math.BigDecimal;
import java.util.Vector;

public class ClaimFormItemStandardForm extends Form 
{

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
	

	public void setFormId(String formId)
	{ this.formId = formId;}
	public String getFormId()
	{ return this.formId;}

	public void onRequest(Event evt) 
	{
		init();
	}

	public void init()
	{
		Application application = Application.getInstance();
		setColumns(2);
        setMethod("POST");

		addChild(new Label("date", "<b>"+Application.getInstance().getMessage("claims.label.date","Date")+"</b>"));
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
/*
		addChild(new Label("category","Category"));
		sb_Category = new SelectBox("sb_Category");
		/// need to have a loop here to populate all active category
//		sb_Category;
		{
         ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule)
               application.getModule(ClaimFormItemCategoryModule.class);
         Vector colCat = new Vector( module.selectObjects("status","act", 0, -1));
			for(int cnt1 = 0; cnt1<colCat.size(); cnt1++)	
			{
				ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);
				sb_Category.addOption(valObj.getId(),valObj.getName());
			}
		}
		sb_Category.setSelectedOptions(new String[]
								{ ClaimFormItemCategoryModule.DEFAULTID});
		addChild(sb_Category);
*/
		addChild(new Label("type","<b>"+Application.getInstance().getMessage("claims.type.name","Type")+"</b>"));
		sb_StandardType = new SelectBox("sb_StandardType");
		{
         ClaimStandardTypeModule module = (ClaimStandardTypeModule)
               application.getModule(ClaimStandardTypeModule.class);
         Vector colCat = new Vector(module.selectObjects("status","act", 0, -1));
         for(int cnt1 = 0; cnt1<colCat.size(); cnt1++)
         {
            ClaimStandardType valObj = (ClaimStandardType) colCat.get(cnt1);
				if(!valObj.getId().equals(ClaimStandardTypeModule.DEFAULTID))
				{
            	sb_StandardType.addOption(valObj.getId(),valObj.getName());
				}
         }
		}
		sb_StandardType.setSelectedOptions(new 
								String[]{ClaimStandardTypeModule.DEFAULTID});
		addChild(sb_StandardType);

		addChild(new Label("Remarks", "<b>"+Application.getInstance().getMessage("claims.label.purpose","Purpose")+"</b>"));
		tf_Remarks = new TextField("tf_Remarks");
		tf_Remarks.setSize("50");
      Validator vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("claims.message.mustNotBeEmpty","Must not be empty"));
		tf_Remarks.addChild(vne);
		addChild(tf_Remarks);
/*
		addChild(new Label("Amount", "Amount"));
		tf_Amount = new TextField("tf_Amount");
		tf_Amount.setSize("5");
        tf_Amount.addChild(new ItemAmountValidator("val_amount", sb_StandardType));
		addChild(tf_Amount);
*/
      addChild(new Label("project","<b>"+Application.getInstance().getMessage("claims.label.project","Project")+"</b>"));
      sb_ProjectId = new SelectBox("sb_Project");
        sb_ProjectId.addOption("","- N/A -");
      {
         ClaimProjectModule module = (ClaimProjectModule)
               application.getModule(ClaimProjectModule.class);
         Vector colCat =new Vector(module.selectObjects("status","act", 0, -1));
         for(int cnt1 = 0; cnt1<colCat.size(); cnt1++)
         {
            ClaimProject valObj = (ClaimProject) colCat.get(cnt1);
            sb_ProjectId.addOption(valObj.getId(),valObj.getName());
         }
      }
		sb_ProjectId.setSelectedOptions(new String[]
								{ClaimProjectModule.DEFAULTID});
		addChild(sb_ProjectId);

		addChild(new Label("a",""));
		addChild(new Label("b",""));
		addChild(new Label("c",""));

		bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.label.addItem","Add Item"));
		addChild(bn_Submit);
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
            } catch(Exception e) {
                return true;
            }

            if(ClaimStandardTypeModule.DEFAULTID.equals(s)) {
                // check the amount
                String v = (String) formField.getValue();
                if(NumberUtils.isNumber(v)) {
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


	public Forward onValidate(Event evt)
	{
		Application application = Application.getInstance();
		ClaimFormItemModule module = (ClaimFormItemModule)
						application.getModule(ClaimFormItemModule.class);
		ClaimFormIndexModule moduleForm = (ClaimFormIndexModule)
						application.getModule(ClaimFormIndexModule.class);
		ClaimFormIndex formObj = moduleForm.selectObject(formId);
		String stdTypeId =(String) sb_StandardType.getSelectedOptions().keySet().iterator().next();

		ClaimFormItem obj = new ClaimFormItem();
		UuidGenerator uuid = UuidGenerator.getInstance();
		obj.setId(uuid.getUuid());
		Log.getLog(this.getClass()).debug(" THE FORM ID IS ................."+formObj.getId());
		obj.setFormId(formObj.getId());
		obj.setTimeFrom(df_TimeFrom.getDate());
		obj.setTimeTo(df_TimeFrom.getDate());
		obj.setTimeFinancial(df_TimeFrom.getDate());
//		obj.setTimeTo(df_TimeTo.getDate());
//		obj.setTimeFinancial(df_TimeFinancial.getDate());
		if(stdTypeId.equals(ClaimStandardTypeModule.DEFAULTID))
		{
			Log.getLog(this.getClass()).debug("ERRRRRRRRRRRRRRRRRRRRRROOOOOOOOOOOOOOOO");
			return new Forward("errorProcessing");
		}
		else
		{
			ClaimStandardTypeModule cstModule = (ClaimStandardTypeModule)
								application.getModule(ClaimStandardTypeModule.class);
			ClaimStandardType cstObj = cstModule.selectObject(stdTypeId);
			obj.setCurrency(cstObj.getCurrency());
			obj.setAmount(cstObj.getAmount());
			obj.setUnitPrice(cstObj.getAmount());
			obj.setRemarks((String)tf_Remarks.getValue());
			obj.setCategoryId(cstObj.getCategory());
		}
		obj.setQty(new BigDecimal("1.00"));
		obj.setStandardTypeId((String) sb_StandardType.getSelectedOptions().keySet().iterator().next());
		//obj.setCategoryId((String) sb_Category.getSelectedOptions().keySet().iterator().next());
		obj.setProjectId((String) sb_ProjectId.getSelectedOptions().keySet().iterator().next());
		Log.getLog(this.getClass()).debug(" =============== 1 ============="+formObj.getId());
//		obj.setDescription((String)tf_Description.getValue());
		/// can set description according to ClaimStandardType

//		obj.setRejectReason((String)tf_RejectReason.getValue());
		obj.setState("pap");
		obj.setStatus("act");

//		obj.setState((String) sb_State.getSelectedOptions().keySet().iterator().next());
//		obj.setStatus((String) sb_Status.getSelectedOptions().keySet().iterator().next());

		Log.getLog(this.getClass()).debug(" =============== 2 ============="+formObj.getId());
      module.addObject(obj);
		Log.getLog(this.getClass()).debug(" =============== 3 ============="+formObj.getId());

		/// if successfully added the ClaimFormItem, must update the ClaimFormAmount

		removeChildren();
		init();

		return new Forward("success");
		//return super.onValidate(evt);
    }
}
