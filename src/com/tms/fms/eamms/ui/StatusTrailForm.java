package com.tms.fms.eamms.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;

import com.tms.fms.eamms.model.EammsFeedsModule;

public class StatusTrailForm extends Form 
{
	private String feedsDetailsId;
	private Collection statusTrailCol;
	
	public StatusTrailForm() {}

	public StatusTrailForm(String s) 
	{
		super(s);
	}
	
	public void init() 
	{
		populateForm();
	}

	public void onRequest(Event evt) 
	{
		 init();
	}
	
	private void populateForm()
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		statusTrailCol = em.getStatusTrail(feedsDetailsId);
	}
	
	public String getDefaultTemplate() 
	{
		return "fms/eamms/statusTrailTemplate";
	}

	public Collection getStatusTrailCol()
	{
		return statusTrailCol;
	}

	public void setStatusTrailCol(Collection statusTrailCol)
	{
		this.statusTrailCol = statusTrailCol;
	}

	public String getFeedsDetailsId()
	{
		return feedsDetailsId;
	}

	public void setFeedsDetailsId(String feedsDetailsId)
	{
		this.feedsDetailsId = feedsDetailsId;
	}
}
