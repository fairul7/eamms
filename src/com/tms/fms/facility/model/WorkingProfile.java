/**
 * 
 */
package com.tms.fms.facility.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DefaultDataObject;
import kacang.util.Log;

/**
 * @author arunkumar
 *
 */
public class WorkingProfile extends DefaultDataObject {
	
	private String workingProfileId;
	private String workingProfileDurationId;
	private String name;
	private String startTime;
	private String endTime;
	private Date startDate;
	private Date endDate;
	private boolean defaultProfile;
	private String createdBy;
	private String modifiedBy;
	private Date createdOn;
	private Date modifiedOn;
	private String workingHours;
	private Map manpowerMap;
	private String manpower;
	private String manpowerId;
	private String description;
	private String wpdManpowerId;
	private String studio1;
	private String studio2;
	private String studio3;
	private String studio4;
	private String studio5;
	private String studio6;
	private String studio7;
	private String studio8;
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the defaultProfile
	 */
	public boolean isDefaultProfile() {
		return defaultProfile;
	}
	/**
	 * @param defaultProfile the defaultProfile to set
	 */
	public void setDefaultProfile(boolean defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return formatTime(endTime);
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		
		return formatTime(startTime);
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the workingProfileId
	 */
	public String getWorkingProfileId() {
		return workingProfileId;
	}
	/**
	 * @param workingProfileId the workingProfileId to set
	 */
	public void setWorkingProfileId(String workingProfileId) {
		this.workingProfileId = workingProfileId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the workingHours
	 */
	public String getWorkingHours() {
		workingHours="";
		if(startTime!=null && endTime!=null){
			workingHours=getStartTime()+" - "+getEndTime();
		}
		return workingHours;
	}
	
	public int getStartTimeHour() {
		int hours=0;
		if(startTime!=null ){
			hours=Integer.parseInt(getStartTime().split(":")[0]);
		}
		return hours;
	}
	
	public int getStartTimeMinute() {
		int minutes=0;
		if(startTime!=null ){
			minutes=Integer.parseInt(getStartTime().split(":")[1]);
		}
		return minutes;
	}
	
	public int getEndTimeHour() {
		int hours=0;
		if(endTime!=null ){
			hours=Integer.parseInt(getEndTime().split(":")[0]);
		}
		return hours;
	}
	
	public int getEndTimeMinute() {
		int minutes=0;
		if(endTime!=null ){
			minutes=Integer.parseInt(getEndTime().split(":")[1]);
		}
		return minutes;
	}
	
	private String formatTime(String time){
		try{
			String[] splitTime=time.split(":");
			if(splitTime[0].length()==1){
				splitTime[0]="0"+splitTime[0];
			}
			if(splitTime[1].length()==1){
				splitTime[1]="0"+splitTime[1];
			}
			return splitTime[0]+":"+splitTime[1];
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return time;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the workingProfileDurationId
	 */
	public String getWorkingProfileDurationId() {
		return workingProfileDurationId;
	}
	/**
	 * @param workingProfileDurationId the workingProfileDurationId to set
	 */
	public void setWorkingProfileDurationId(String workingProfileDurationId) {
		this.workingProfileDurationId = workingProfileDurationId;
	}
	/**
	 * @return the manpowerMap
	 */
	public Map getManpowerMap() {
		return manpowerMap;
	}
	/**
	 * @param manpowerMap the manpowerMap to set
	 */
	public void setManpowerMap(Map manpowerMap) {
		this.manpowerMap = manpowerMap;
	}
	
	public String[] getUsers() {
		if(manpowerMap!=null){
			String[] users=new String[manpowerMap.size()];
			int i=0;
			for(Iterator itr=manpowerMap.keySet().iterator();itr.hasNext();i++){
				users[i]=(String)itr.next(); 
			}
			return users;
		}
		return new String[]{};
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getManpower() {
		return manpower;
	}
	public void setManpower(String manpower) {
		this.manpower = manpower;
	}
	public String getManpowerId() {
		return manpowerId;
	}
	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}
	public String getWpdManpowerId() {
		return wpdManpowerId;
	}
	public void setWpdManpowerId(String wpdManpowerId) {
		this.wpdManpowerId = wpdManpowerId;
	}
	public String getStudio1() {
		return studio1;
	}
	public void setStudio1(String studio1) {
		this.studio1 = studio1;
	}
	public String getStudio2() {
		return studio2;
	}
	public void setStudio2(String studio2) {
		this.studio2 = studio2;
	}
	public String getStudio3() {
		return studio3;
	}
	public void setStudio3(String studio3) {
		this.studio3 = studio3;
	}
	public String getStudio4() {
		return studio4;
	}
	public void setStudio4(String studio4) {
		this.studio4 = studio4;
	}
	public String getStudio5() {
		return studio5;
	}
	public void setStudio5(String studio5) {
		this.studio5 = studio5;
	}
	public String getStudio6() {
		return studio6;
	}
	public void setStudio6(String studio6) {
		this.studio6 = studio6;
	}
	public String getStudio7() {
		return studio7;
	}
	public void setStudio7(String studio7) {
		this.studio7 = studio7;
	}
	public String getStudio8() {
		return studio8;
	}
	public void setStudio8(String studio8) {
		this.studio8 = studio8;
	}
	
}
