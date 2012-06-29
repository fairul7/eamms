/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormItemCategory;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Date;


public class ClaimFormItemCategoryEditForm extends ClaimFormItemCategoryForm 
{
	String id;

	public String getId() 
	{ return id; }

	public void setId(String id) 
	{ this.id = id; }

	public void onRequest(Event evt) 
	{ populateForm(); }

	public void populateForm() 
	{
		if(getId() != null)
		{
			Application application = Application.getInstance();
			ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule)
				application.getModule(ClaimFormItemCategoryModule.class);

			try 
			{
                // load contact
				ClaimFormItemCategory obj = module.selectObject(getId());
				if(obj==null){throw new DataObjectNotFoundException() ;}
				tf_Code.setValue(obj.getCode());
				tf_Name.setValue(obj.getName());
				tf_Description.setValue(obj.getDescription());
                sb_Type.setSelectedOption(obj.getType());
//				df_TimeEdit.setDate(obj.getTimeEdit());
//				sb_State.setSelectedOptions(new String[]{obj.getState()});
//				sb_Status.setSelectedOptions(new String[]{obj.getStatus()});
         } 
			catch(DataObjectNotFoundException e) 
			{
				Log.getLog(getClass()).error("Claim FormItemCategory" + getId() 
						+ " not found"); init();
			}
		}
    }

	public Forward onValidate(Event evt) 
	{
		Forward myForward = null;
		if (bn_Cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
			myForward= new Forward(FORWARD_CANCEL);
		} else {
			myForward = updateCategory();
		}
		return myForward;
	}

	private Forward updateCategory() {

		Application application = Application.getInstance();
		ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule)
				application.getModule(ClaimFormItemCategoryModule.class);

		String userId = getWidgetManager().getUser().getId();

		ClaimFormItemCategory obj = module.selectObject(getId());

		obj.setCode((String)tf_Code.getValue());
		obj.setName((String)tf_Name.getValue());
		obj.setDescription((String)tf_Description.getValue());
        obj.setType((String)sb_Type.getSelectedOptions().keySet().iterator().next());
		obj.setUserEdit(userId);
		obj.setTimeEdit(new Date());
//		obj.setState((String)sb_State.getSelectedOptions().keySet().iterator().next());
//		obj.setStatus((String)sb_Status.getSelectedOptions().keySet().iterator().next());

		module.updateObject(obj);

		return new Forward("updated");

	}

	public boolean isEditMode() {
		return true;
	}

}
