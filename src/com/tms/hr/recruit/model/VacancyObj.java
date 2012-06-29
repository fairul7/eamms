package com.tms.hr.recruit.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.User;
import kacang.util.UuidGenerator;

public class VacancyObj extends DefaultDataObject{
	private String vacancyTempCode; //for template Code
	private String hodId;
	
	private String vacancyCode; //for vacancyCode
	private String positionId;
	private String countryId;
	private String departmentId;
	private int noOfPosition;
	private boolean priority;
	private String priorityName;
	private String responsibilities;
	private String requirements;
	private Date startDate;
	private Date endDate;
	
	private int noOfPositionOffered;
	private String noOfPositionDetail;
	private String createdBy;
	private String lastUpdatedBy;
	private Date createdDate;
	private Date lastUpdatedDate;
	private boolean equalNoOfPosition=false;
	
	private String messageBody;
	
	//total counting 
	private int totalApplied;
	private int totalShortlisted;
	private int totalScheduled;
	//private int totalScheduleRejected;
	private int totalReScheduled; //new
	private int totalReScheduledRejected; //new
	private int totalJobOffered;
	private int totalInterviewUnsuccessful;
	private int totalJobAccepted;
	private int totalJobRejected;
	private int totalBlackListed;
	private int totalViewed;
	private int totalAnotherInterview;//new
	
	private String code;
	private String carrerName;
	
	//audit
	private String auditId;
	private String userId;
	private String name;
	private String applicantId;
	private String actionTaken;
	private Date auditDate;
	private String username;
	
	private String employeeOppor;
	
	//vacancyHit
	private String ipAddr;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public int getNoOfPosition() {
		return noOfPosition;
	}
	public void setNoOfPosition(int noOfPosition) {
		this.noOfPosition = noOfPosition;
	}
	public int getNoOfPositionOffered() {
		return noOfPositionOffered;
	}
	public void setNoOfPositionOffered(int noOfPositionOffered) {
		this.noOfPositionOffered = noOfPositionOffered;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public boolean isPriority() {
		return priority;
	}
	public void setPriority(boolean priority) {
		this.priority = priority;
	}
	public String getRequirements() {
		return requirements;
	}
	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getVacancyCode() {
		return vacancyCode;
	}
	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}
	public String getVacancyTempCode() {
		return vacancyTempCode;
	}
	public void setVacancyTempCode(String vacancyTempCode) {
		this.vacancyTempCode = vacancyTempCode;
	}
	
	//total counting 
	public int getTotalApplied() {
		return totalApplied;
	}
	public void setTotalApplied(int totalApplied) {
		this.totalApplied = totalApplied;
	}
	public void setTotalApplied(String vacancyCode, String updateType,  boolean toMinus) {
		boolean flag=false;
		if("totalApplied".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalApplied = getCurrentTotal("totalApplied", vacancyCode, flag, toMinus);
		this.totalApplied = totalApplied;
	}
	
	public int getTotalBlackListed() {
		return totalBlackListed;
	}
	public void setTotalBlackListed(int totalBlackListed) {
		this.totalBlackListed = totalBlackListed;
	}
	public void setTotalBlackListed(String vacancyCode, String updateType,  boolean toMinus) {
		boolean flag=false;
		if("totalBlackListed".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalBlackListed = getCurrentTotal("totalBlackListed", vacancyCode, flag, toMinus);
		this.totalBlackListed = totalBlackListed;
	}
	
	public int getTotalInterviewUnsuccessful() {
		return totalInterviewUnsuccessful;
	}
	public void setTotalInterviewUnsuccessful(int totalInterviewUnsuccessful) {
		this.totalInterviewUnsuccessful = totalInterviewUnsuccessful;
	}
	public void setTotalInterviewUnsuccessful(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalInterviewUnsuccessful".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalInterviewUnsuccessful = getCurrentTotal("totalInterviewUnsuccessful", vacancyCode, flag, toMinus);
		this.totalInterviewUnsuccessful = totalInterviewUnsuccessful;
	}
	
	public int getTotalJobAccepted() {
		return totalJobAccepted;
	}
	public void setTotalJobAccepted(int totalJobAccepted) {
		this.totalJobAccepted = totalJobAccepted;
	}
	public void setTotalJobAccepted(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalJobAccepted".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalJobAccepted = getCurrentTotal("totalJobAccepted", vacancyCode, flag, toMinus);
		this.totalJobAccepted = totalJobAccepted;
	}
	
	public int getTotalJobOffered() {
		return totalJobOffered;
	}
	public void setTotalJobOffered(int totalJobOffered) {
		this.totalJobOffered = totalJobOffered;
	}
	public void setTotalJobOffered(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalJobOffered".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalJobOffered = getCurrentTotal("totalJobOffered", vacancyCode, flag, toMinus);
		this.totalJobOffered = totalJobOffered;
	}
	
	public int getTotalJobRejected() {
		return totalJobRejected;
	}
	public void setTotalJobRejected(int totalJobRejected) {
		this.totalJobRejected = totalJobRejected;
	}
	public void setTotalJobRejected(String vacancyCode, String updateType,  boolean toMinus) {
		boolean flag=false;
		if("totalJobRejected".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalJobRejected = getCurrentTotal("totalJobRejected", vacancyCode, flag, toMinus);
		this.totalJobRejected = totalJobRejected;
	}
	
	public int getTotalScheduled() {
		return totalScheduled;
	}
	public void setTotalScheduled(int totalScheduled) {
		this.totalScheduled = totalScheduled;
	}
	public void setTotalScheduled(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalScheduled".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalScheduled = getCurrentTotal("totalScheduled", vacancyCode, flag, toMinus);
		this.totalScheduled = totalScheduled;
	}
	
	public int getTotalAnotherInterview() {
		return totalAnotherInterview;
	}
	public void setTotalAnotherInterview(int totalAnotherInterview) {
		this.totalAnotherInterview = totalAnotherInterview;
	}
	public void setTotalAnotherInterview(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalAnotherInterview".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalAnotherInterview = getCurrentTotal("totalAnotherInterview", vacancyCode, flag, toMinus);
		this.totalAnotherInterview = totalAnotherInterview;
	}
	
	/*public int getTotalScheduleRejected() {
		return totalScheduleRejected;
	}
	public void setTotalScheduleRejected(int totalScheduleRejected) {
		this.totalScheduleRejected = totalScheduleRejected;
	}
	public void setTotalScheduleRejected(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalScheduleRejected".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalScheduleRejected = getCurrentTotal("totalScheduleRejected", vacancyCode, flag, toMinus);
		this.totalScheduleRejected = totalScheduleRejected;
	}*/
	
	public int getTotalShortlisted() {
		return totalShortlisted;
	}
	public void setTotalShortlisted(int totalShortlisted) {
		this.totalShortlisted = totalShortlisted;
	}
	public void setTotalShortlisted(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalShortlisted".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalShortlisted = getCurrentTotal("totalShortlisted", vacancyCode, flag, toMinus);
		this.totalShortlisted = totalShortlisted;
	}
	
	public int getTotalViewed() {
		return totalViewed;
	}
	/*public int getTotalViewed(String vacancyCode){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection vacancyTotalCol=ram.lookUpVacancyTotal(vacancyCode);
		HashMap map = (HashMap) vacancyTotalCol.iterator().next();
		int total=Integer.parseInt(map.get("totalViewed").toString());
		
		totalViewed=total;
		
		return totalViewed;
	}*/
	
	public void setTotalViewed(int totalViewed) {
		this.totalViewed = totalViewed;
	}
	public void setTotalViewed(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalViewed".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalViewed = getCurrentTotal("totalViewed", vacancyCode, flag, toMinus);
		this.totalViewed = totalViewed;
	}
	
	public int getTotalReScheduled() {
		return totalReScheduled;
	}
	public void setTotalReScheduled(int totalReScheduled) {
		this.totalReScheduled = totalReScheduled;
	}
	public void setTotalReScheduled(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalReScheduled".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalReScheduled = getCurrentTotal("totalReScheduled", vacancyCode, flag, toMinus);
		this.totalReScheduled = totalReScheduled;
	}
	
	public int getTotalReScheduledRejected() {
		return totalReScheduledRejected;
	}
	public void setTotalReScheduledRejected(int totalReScheduledRejected) {
		this.totalReScheduledRejected = totalReScheduledRejected;
	}
	public void setTotalReScheduledRejected(String vacancyCode, String updateType, boolean toMinus) {
		boolean flag=false;
		if("totalReScheduledRejected".equals(updateType)){
			flag=true;
			if(toMinus)
				flag=false;
		}else
			toMinus=false;
		
		int totalReScheduledRejected = getCurrentTotal("totalReScheduledRejected", vacancyCode, flag, toMinus);
		this.totalReScheduledRejected = totalReScheduledRejected;
	}
	
	//	get the total info
	public int getCurrentTotal(String key, String vacancyCode, boolean flag, boolean toMinus){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection vacancyTotalCol=ram.lookUpVacancyTotal(vacancyCode);
		HashMap map = (HashMap) vacancyTotalCol.iterator().next();
		int total=Integer.parseInt(map.get(key).toString());
		
		if(flag)
			total=total + 1;
		
		if(toMinus)
			total=total - 1;
		
		return total;
	}
	
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public String getCarrerName() {
		return carrerName;
	}
	public void setCarrerName(String carrerName) {
		this.carrerName = carrerName;
	}
	public String getNoOfPositionDetail() {
		return noOfPositionDetail;
	}
	public void setNoOfPositionDetail(String noOfPositionDetail) {
		this.noOfPositionDetail = noOfPositionDetail;
	}
	public boolean isEqualNoOfPosition() {
		return equalNoOfPosition;
	}
	public void setEqualNoOfPosition(boolean equalNoOfPosition) {
		this.equalNoOfPosition = equalNoOfPosition;
	}
	public String getHodId() {
		return hodId;
	}
	public void setHodId(String hodId) {
		this.hodId = hodId;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	
	//audit trail
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
	
	public String getEmployeeOppor() {
		return employeeOppor;
	}
	public void setEmployeeOppor(String employeeOppor) {
		this.employeeOppor = employeeOppor;
	}
	//set and insert into AUDIT
	public void setAndInsertAudit(String vacancyCode, String applicantId, String actionTaken){
		Application app = Application.getInstance();
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		
		VacancyObj vacancyObj = new VacancyObj();
		Date getDate = new Date(); //set creation date
		UuidGenerator uuid = UuidGenerator.getInstance(); //generate id
		User user=app.getCurrentUser(); //get user id
		String getUserId="";
		String getVacancyCode="";
		String getApplicantId="";
		String getActionTaken="";
		String getName="";
		if(user!=null && !user.equals("") && !user.getId().equals("anonymous"))
			getUserId = user.getId();
		else{
			getUserId = "-";
		}
		//pre-set
		vacancyObj.setUserId(getUserId);
		vacancyObj.setAuditId(uuid.getUuid());
		vacancyObj.setAuditDate(getDate);
		
		if(!vacancyCode.equals(""))
			getVacancyCode = vacancyCode;
		else
			getVacancyCode = "-";
		
		if(!applicantId.equals("")){
			getApplicantId = applicantId;
			Collection col=ram.loadApplicantPersonal(getApplicantId);
			HashMap map = (HashMap)col.iterator().next();
			getName=map.get("name").toString();
		}else{
			getApplicantId = "-";
			getName = "-";
		}
		if(!actionTaken.equals(""))
			getActionTaken = actionTaken;
		else
			getActionTaken = "-";
		
		vacancyObj.setVacancyCode(getVacancyCode);	
		vacancyObj.setApplicantId(getApplicantId);
		vacancyObj.setActionTaken(getActionTaken);
		vacancyObj.setName(getName);
		rm.insertAudit(vacancyObj);//insert into db
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//validation date
	public boolean checkDate(Date date) throws Exception{
		boolean flag=false;
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String nowDate = sdf.format(todayDate);
	
		if(date.before(sdf.parse(nowDate)))//this will check for today and onward
			flag=true;
		
		return flag;
	}
	
	public boolean checkDate(Date startDate, Date endDate){
		boolean flag=false;
		/*Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String nowDate = sdf.format(todayDate);*/
		
		if(endDate.equals(startDate) || endDate.before(startDate))//this will check same start date and end date || end date is bigger then start date
			flag=true;

		return flag;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	
	
}













