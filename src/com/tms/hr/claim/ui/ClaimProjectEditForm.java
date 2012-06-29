/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimProject;
import com.tms.hr.claim.model.ClaimProjectModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ClaimProjectEditForm extends ClaimProjectForm 
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
			ClaimProjectModule module = (ClaimProjectModule)
				application.getModule(ClaimProjectModule.class);

			try 
			{
                // load contact
				ClaimProject obj = module.selectObject(getId());
				if(obj==null){throw new DataObjectNotFoundException() ;}
//				tf_FkPcc.setValue(obj.getFkPcc());
				tf_Name.setValue(obj.getName());
				rtb_Description.setValue(obj.getDescription());
				sb_Status.setSelectedOptions(new String[]{obj.getStatus()});
         } 
			catch(DataObjectNotFoundException e) 
			{
				e.printStackTrace();
				Log.getLog(getClass()).error("Claim Project" + getId() 
						+ " not found"); init();
			}
		}
    }

	public Forward onValidate(Event evt) 
	{
		Forward myForward = null;
		if (isEditMode()) {
			if (bn_Cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
				myForward= new Forward(FORWARD_CANCEL);
			} else {
				myForward = updateClaim();
			}
		}
		return myForward;
	}

	private Forward updateClaim() {

		Application application = Application.getInstance();
		ClaimProjectModule module = (ClaimProjectModule)
				application.getModule(ClaimProjectModule.class);

		ClaimProject obj = module.selectObject(getId());
		Log.getLog(this.getClass()).debug(" Object id is "+ getId());

		if(obj==null){ Log.getLog(this.getClass()).debug(" NULL OBJECT!!!!!!!!!!!!!!!!!!!!! "); }
		obj.setId(getId());

//		obj.setFkPcc((String)tf_FkPcc.getValue());
		obj.setName((String)tf_Name.getValue());
		obj.setDescription((String)rtb_Description.getValue());
		obj.setStatus((String)sb_Status.getSelectedOptions().keySet().iterator().next());

		module.updateObject(obj);

		return new Forward("updateSuccess");
		//return null;
	}

	public boolean isEditMode() {
		return true;
	}

}
