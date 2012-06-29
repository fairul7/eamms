package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;

public class PrintInternalCheckOutForm extends Form {
	
	protected String groupId;
	protected FacilityObject fo = new FacilityObject();
	protected Collection childItems=new ArrayList();
	private Date checkedOutDate;
	private String userLogin; 
	
	public PrintInternalCheckOutForm() {
	}

	public PrintInternalCheckOutForm(String s) {super(s);}

	public void init() {
	}
	
	public void onRequest(Event arg0) {
		init();
		populateRequest();
	}
	
	private void populateRequest(){
		setMethod("post");
		Application app = Application.getInstance();
		FacilityModule module  = (FacilityModule)app.getModule(FacilityModule.class);
		
		// 1. Get the facility object from the latest items checked out
		fo = module.getLatestItemCheckedOut();		
		
		SecurityService security = (SecurityService)app.getService(SecurityService.class);
        String userCheckout = "";
        String userCheckin = "";
        try{
        	userCheckout = security.getUser(fo.getCheckout_by()).getName();
        	fo.setCheckout_by(userCheckout);
        }catch(Exception e){
        	//Log.getLog(getClass()).error("Error getUser(1)", e);
        }
        try{
        	userCheckin = security.getUser(fo.getCheckin_by()).getName();
        	fo.setCheckin_by(userCheckin);
        }catch(Exception e){
        	//Log.getLog(getClass()).error("Error getUser(1)", e);
        }
		
		// 2. Get the collection of items based on FacilityObject.getGroupId()		
		if (fo.getGroupId() != null && !"".equals(fo.getGroupId())){
		
			Collection childrenTemp = module.getFacilityByGroupId(fo.getGroupId());
			childItems = new ArrayList();
			if (childrenTemp != null && childrenTemp.size()>0){
				for(Iterator iterator = childrenTemp.iterator(); iterator.hasNext();) {
					FacilityObject foTemp = (FacilityObject) iterator.next();
					try{
			        	userCheckout = security.getUser(foTemp.getCheckout_by()).getName();
			        	foTemp.setCheckout_by(userCheckout);
			        }catch(Exception e){
			        	Log.getLog(getClass()).error("Error getUser(1)", e);
			        }
					if (foTemp.getCheckin_by() != null) {
				        try{
				        	userCheckin = security.getUser(foTemp.getCheckin_by()).getName();
				        	foTemp.setCheckin_by(userCheckin);
				        }catch(Exception e){
				        	Log.getLog(getClass()).error("Error getUser(1)", e);
				        }
					}
			        childItems.add(foTemp);
				}
					
			}
			
		}
		
		checkedOutDate = new Date();
		userLogin = Application.getInstance().getCurrentUser().getName();
		
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/printInternalCheckOut";
	}

	public Collection getChildItems() {
		return childItems;
	}

	public void setChildItems(Collection childItems) {
		this.childItems = childItems;
	}

	public Date getCheckedOutDate() {
		return checkedOutDate;
	}

	public void setCheckedOutDate(Date checkedOutDate) {
		this.checkedOutDate = checkedOutDate;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public FacilityObject getFo() {
		return fo;
	}

	public void setFo(FacilityObject fo) {
		this.fo = fo;
	}
	
}
