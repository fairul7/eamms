
package com.tms.collab.myfolder.model;

import kacang.model.DefaultDataObject;


public class MyFolderQuota extends DefaultDataObject{
	
	private String userId;
	private long quota;
	private long usedSpace;
	
	/**
	 * @return Returns the quota.
	 */
	public long getQuota() {
		return quota;
	}
	/**
	 * @param quota The quota to set.
	 */
	public void setQuota(long quota) {
		this.quota = quota;
	}
	/**
	 * @return Returns the used_space.
	 */
	public long getUsedSpace() {
		return usedSpace;
	}
	/**
	 * @param used_space The used_space to set.
	 */
	public void setUsedSpace(long usedSpace) {
		this.usedSpace = usedSpace;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
