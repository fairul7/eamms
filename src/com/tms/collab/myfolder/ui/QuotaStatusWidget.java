/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.util.Properties;

import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.messaging.model.Util;
import com.tms.collab.myfolder.model.MyFolderModule;
import com.tms.collab.myfolder.model.MyFolderQuota;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QuotaStatusWidget extends Widget{
	
	protected long quota1;
	protected double usedSpace1;
	protected MyFolderQuota quota;
	
	public void onRequest(Event evt){
		
		try{
			User user = Util.getUser(evt);
			Properties properties = Application.getInstance().getProperties();
    		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
    		
    		quota1 = mf.getUserQuota(user.getId());
    		
    		if(quota1 == 0){
    			quota1 = Long.parseLong(properties.getProperty("myfolder.space.quota"));
    			quota1 = quota1 * 1024;
    		}
    		
    		usedSpace1 = mf.getUserUsedSpace(user.getId());
		}
		catch(Exception e){
			Log.getLog(getClass()).error("Unable to retrieve quota status: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve quota status: " + e.toString());
		}
	}
	
	public String getDefaultTemplate(){
		return "myfolder/quotaStatus";
	}
	
	
	/**
	 * @return Returns the quota.
	 */
	public MyFolderQuota getQuota() {
		return quota;
	}
	/**
	 * @param quota The quota to set.
	 */
	public void setQuota(MyFolderQuota quota) {
		this.quota = quota;
	}
	/**
	 * @return Returns the quota1.
	 */
	public long getQuota1() {
		return quota1;
	}
	/**
	 * @param quota1 The quota1 to set.
	 */
	public void setQuota1(long quota1) {
		this.quota1 = quota1;
	}
	/**
	 * @return Returns the usedSpace1.
	 */
	public double getUsedSpace1() {
		return usedSpace1;
	}
	/**
	 * @param usedSpace1 The usedSpace1 to set.
	 */
	public void setUsedSpace1(long usedSpace1) {
		this.usedSpace1 = usedSpace1;
	}
}
