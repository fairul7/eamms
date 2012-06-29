/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Date;

public class ClaimFormIndexEditForm extends ClaimFormIndexForm 
{
	String id;
    protected String claimant;

    public String getId()
	{ return id; }

	public void setId(String id) 
	{ this.id = id; }

	public void onRequest(Event evt) 
	{ populateForm(); }

	public String getState()
	{
		Application application = Application.getInstance();
		ClaimFormIndexModule module = (ClaimFormIndexModule)
            application.getModule(ClaimFormIndexModule.class);
		ClaimFormIndex obj = module.selectObject(getId());
		return obj.getState();
	}


	public void populateForm() 
	{
		if(getId() != null)
		{
			Log.getLog(this.getClass()).debug("-----------------------------");
			Log.getLog(this.getClass()).debug("The FormID:"+getId());
			Application application = Application.getInstance();
			ClaimFormIndexModule module = (ClaimFormIndexModule)
				application.getModule(ClaimFormIndexModule.class);

			try 
			{
                // load contact
				ClaimFormIndex obj = module.selectObject(getId());
				if(obj==null){throw new DataObjectNotFoundException() ;}
//			df_TimeEdit.setDate(obj.getTimeEdit());
//				sb_UserOriginator.setSelectedOptions(new String[]{obj.getUserOriginator()});
//				sb_UserApprover1.setSelectedOptions(new String[]{obj.getUserApprover1()});
//				sb_UserApprover2.setSelectedOptions(new String[]{obj.getUserApprover2()});
//				sb_Currency.setSelectedOptions(new String[]{obj.getCurrency()});
			int nOwners = ClaimConfigAssistant.countPossibleOptions(
                                 getWidgetManager().getUser().getId(),
                                 ClaimConfigAssistant.namespace);
/*
	      if(nOwners>0)
	      {
				if(sb_UserOwner==null)
				sb_UserOwner = new SelectBox();
         	sb_UserOwner.addOption(getWidgetManager().getUser().getId(),
                           getWidgetManager().getUser().getName());
         	ClaimConfigAssistant.addPossibleOptions(sb_UserOwner,
                                 getWidgetManager().getUser().getId(),
                                 ClaimConfigAssistant.namespace);
         	sb_UserOwner.setSelectedOptions(
							new String[]{obj.getUserOwner()});
			}
*/			


				tf_Remarks.setValue(obj.getRemarks());
                //tf_Amount.setValue(obj.getAmount());
				lb_Amount.setText((String) obj.getAmountStr());

         } 
			catch(DataObjectNotFoundException e) 
			{
				e.printStackTrace();
				Log.getLog(getClass()).error("Claim FormIndex" + getId() 
						+ " not found"); init();
			}
		}


        if(sb_UserOwner!=null)
      {
         setClaimant((String)sb_UserOwner.getSelectedOptions().keySet().iterator().next());
      }
      else
      {

         setClaimant("");
      }
    }

	public Forward onValidate(Event evt) 
	{
		Application application = Application.getInstance();
		ClaimFormIndexModule module = (ClaimFormIndexModule)
				application.getModule(ClaimFormIndexModule.class);

		ClaimFormIndex obj = module.selectObject(getId());

		obj.setId(getId());
//		obj.setTimeEdit(df_TimeEdit.getDate());
		obj.setTimeEdit(new Date());
		obj.setUserOriginator(getWidgetManager().getUser().getId());
      if(sb_UserOwner!=null)
      {
         obj.setUserOwner((String)sb_UserOwner.getSelectedOptions().keySet().iterator().next());
         setClaimant((String)sb_UserOwner.getSelectedOptions().keySet().iterator().next());
      }
      else
      {
         obj.setUserOwner(getWidgetManager().getUser().getId());
         setClaimant("");
      }

		obj.setApprovalLevelRequired(obj.getNumberOfApprover());
		obj.setRemarks((String)tf_Remarks.getValue());
        module.updateObject(obj);

		//return new Forward("update");
		//return super.onValidate(evt);
		return null;
	}


      public String getClaimant() {
        return claimant;
    }

    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

}
