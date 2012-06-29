package com.tms.hr.recruit.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.ui.Event;

public class ApplicantObj extends DefaultDataObject{
	
	//media obj resume
	private StorageFile storageFile; 
		
	//applicant status
	private String vacancyCode;
	private String applicantStatus;
	private Date jobOfferDate;
	private boolean jobOfferLetterSent;
	private String jobOfferRemark;
	private int totalInterview;
	private String jobOfferAdminRemark;
	
	//personal
	private String positionApplied;
	private String positionAppliedDesc;
	private String applicantId;
	private String name;
	private String age;
	private Date dob;
	private String nirc;
	private boolean gender;
	private String maritalStatus;
	private int noOfChild;
	private String nationality;
	private String email;
	private String mobileNo;
	private String telephoneNo;
	private String address;
	private String postalCode;
	private String country;
	private String state;
	private String city;
	private Date createdDate;
	private Date dateApplied;
	private boolean applicantBlacklisted;
	
	//employement details
	private String empId;
	private String companyName;
	private String positionName;
	private String positionLvl;
	private String positionLvlDesc;
	private String reasonForLeave;
	
	//employement working exp "yes/no"
	private String ttype;
	//private String tyear;
	//private String tmonth;
	private String yearOfWorkingExp;
	
	//edu details
	private String eduId;
	private String highEduLevel;
	private String highEduLevelDesc;
	private String institute;
	private String courseTitle;
	private String grade;
	
	//skills
	private String skillId;
	private String proficiency;
	private String proficiencyDesc;
	private String skill;
	private String yearOfExpSkill;
	
	//language
	private String languageId;
	private String language;
	private String languageDesc;
	private String spoken;
	private String spokenDesc;
	private String written;
	private String writtenDesc;
	
	//additional Info
	private String resumePath;
	private String expectedTypeSalary;
	private String expectedTypeSalaryDesc;
	private Float expectedSalary;
	private boolean negotiable;
	private String willingTravel;
	private String willingRelocate;
	private String ownTransport;
	
	//to all
	private String startDate;
	private String endDate;
	private String byPassValidation;
	
	//
	private String applicantStatusId;
	
	//getter setter edu details
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getEduId() {
		return eduId;
	}
	public void setEduId(String eduId) {
		this.eduId = eduId;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getHighEduLevel() {
		return highEduLevel;
	}
	public void setHighEduLevel(String highEduLevel) {
		this.highEduLevel = highEduLevel;
	}
	public String getHighEduLevelDesc() {
		return highEduLevelDesc;
	}
	public void setHighEduLevelDesc(String highEduLevelDesc) {
		this.highEduLevelDesc = highEduLevelDesc;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	//getter setter employment details
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getPositionLvl() {
		return positionLvl;
	}
	public void setPositionLvl(String positionLvl) {
		this.positionLvl = positionLvl;
	}
	public String getPositionLvlDesc() {
		return positionLvlDesc;
	}
	public void setPositionLvlDesc(String positionLvlDesc) {
		this.positionLvlDesc = positionLvlDesc;
	}
	public String getReasonForLeave() {
		return reasonForLeave;
	}
	public void setReasonForLeave(String reasonForLeave) {
		this.reasonForLeave = reasonForLeave;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	//	employement working exp "yes/no"
	public String getYearOfWorkingExp() {
		return yearOfWorkingExp;
	}
	public void setYearOfWorkingExp(String yearOfWorkingExp) {
		this.yearOfWorkingExp = yearOfWorkingExp;
	}
	/*public String getTmonth() {
		return tmonth;
	}
	public void setTmonth(String tmonth) {
		this.tmonth = tmonth;
	}*/
	public String getTtype() {
		return ttype;
	}
	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	
	/*public String getTyear() {
		return tyear;
	}
	public void setTyear(String tyear) {
		this.tyear = tyear;
	}*/
	
	//Skills
	public String getProficiency() {
		return proficiency;
	}
	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}
	public String getProficiencyDesc() {
		return proficiencyDesc;
	}
	public void setProficiencyDesc(String proficiencyDesc) {
		this.proficiencyDesc = proficiencyDesc;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public String getYearOfExpSkill() {
		return yearOfExpSkill;
	}
	public void setYearOfExpSkill(String yearOfExpSkill) {
		this.yearOfExpSkill = yearOfExpSkill;
	}
	
	//language
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLanguageDesc() {
		return languageDesc;
	}
	public void setLanguageDesc(String languageDesc) {
		this.languageDesc = languageDesc;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getSpoken() {
		return spoken;
	}
	public void setSpoken(String spoken) {
		this.spoken = spoken;
	}
	public String getSpokenDesc() {
		return spokenDesc;
	}
	public void setSpokenDesc(String spokenDesc) {
		this.spokenDesc = spokenDesc;
	}
	public String getWritten() {
		return written;
	}
	public void setWritten(String written) {
		this.written = written;
	}
	public String getWrittenDesc() {
		return writtenDesc;
	}
	public void setWrittenDesc(String writtenDesc) {
		this.writtenDesc = writtenDesc;
	}
	
	//additional info
	public String getExpectedTypeSalary() {
		return expectedTypeSalary;
	}
	public void setExpectedTypeSalary(String expectedTypeSalary) {
		this.expectedTypeSalary = expectedTypeSalary;
	}
	public boolean isNegotiable() {
		return negotiable;
	}
	public void setNegotiable(boolean negotiable) {
		this.negotiable = negotiable;
	}
	public String getOwnTransport() {
		return ownTransport;
	}
	public void setOwnTransport(String ownTransport) {
		this.ownTransport = ownTransport;
	}
	public String getResumePath() {
		return resumePath;
	}
	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}
	public String getWillingRelocate() {
		return willingRelocate;
	}
	public void setWillingRelocate(String willingRelocate) {
		this.willingRelocate = willingRelocate;
	}
	public String getWillingTravel() {
		return willingTravel;
	}
	public void setWillingTravel(String willingTravel) {
		this.willingTravel = willingTravel;
	}
	public String getExpectedTypeSalaryDesc() {
		return expectedTypeSalaryDesc;
	}
	public void setExpectedTypeSalaryDesc(String expectedTypeSalaryDesc) {
		this.expectedTypeSalaryDesc = expectedTypeSalaryDesc;
	}
	public Float getExpectedSalary() {
		return expectedSalary;
	}
	public void setExpectedSalary(Float expectedSalary) {
		this.expectedSalary = expectedSalary;
	}
	
	//personal
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public boolean isApplicantBlacklisted() {
		return applicantBlacklisted;
	}
	public void setApplicantBlacklisted(boolean applicantBlacklisted) {
		this.applicantBlacklisted = applicantBlacklisted;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNirc() {
		return nirc;
	}
	public void setNirc(String nirc) {
		this.nirc = nirc;
	}
	public int getNoOfChild() {
		return noOfChild;
	}
	public void setNoOfChild(int noOfChild) {
		this.noOfChild = noOfChild;
	}
	public String getPositionApplied() {
		return positionApplied;
	}
	public void setPositionApplied(String positionApplied) {
		this.positionApplied = positionApplied;
	}
	public String getPositionAppliedDesc() {
		return positionAppliedDesc;
	}
	public void setPositionAppliedDesc(String positionAppliedDesc) {
		this.positionAppliedDesc = positionAppliedDesc;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public String getByPassValidation() {
		return byPassValidation;
	}
	public void setByPassValidation(String byPassValidation) {
		this.byPassValidation = byPassValidation;
	}
	
	//storage resume file
	public StorageFile getStorageFile() {
		return storageFile;
	}
	public void setStorageFile(StorageFile storageFile) {
		this.storageFile = storageFile;
	}
	
	//applicant status
	public String getApplicantStatus() {
		return applicantStatus;
	}
	public void setApplicantStatus(String applicantStatus) {
		this.applicantStatus = applicantStatus;
	}
	
	public Date getJobOfferDate() {
		return jobOfferDate;
	}
	public void setJobOfferDate(Date jobOfferDate) {
		this.jobOfferDate = jobOfferDate;
	}
	public boolean isJobOfferLetterSent() {
		return jobOfferLetterSent;
	}
	public void setJobOfferLetterSent(boolean jobOfferLetterSent) {
		this.jobOfferLetterSent = jobOfferLetterSent;
	}
	public String getJobOfferRemark() {
		return jobOfferRemark;
	}
	public void setJobOfferRemark(String jobOfferRemark) {
		this.jobOfferRemark = jobOfferRemark;
	}
	public int getTotalInterview() {
		return totalInterview;
	}
	public void setTotalInterview(int totalInterview) {
		this.totalInterview = totalInterview;
	}
	public String getVacancyCode() {
		return vacancyCode;
	}
	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}
	public Date getDateApplied() {
		return dateApplied;
	}
	public void setDateApplied(Date dateApplied) {
		this.dateApplied = dateApplied;
	}
	public String getApplicantStatusId() {
		return applicantStatusId;
	}
	public void setApplicantStatusId(String applicantStatusId) {
		this.applicantStatusId = applicantStatusId;
	}
	public String getJobOfferAdminRemark() {
		return jobOfferAdminRemark;
	}
	public void setJobOfferAdminRemark(String jobOfferAdminRemark) {
		this.jobOfferAdminRemark = jobOfferAdminRemark;
	}
	
	//validation for editing status
	//	validate hasEditStatus
	public boolean validateEditStatus(Event evt){
		boolean hasEditStatus=false;
		if(evt.getRequest().getSession().getAttribute("editData")!=null){
			Collection editDataCol = (Collection)evt.getRequest().getSession().getAttribute("editData");
			if(editDataCol!=null && editDataCol.size() > 0){
				HashMap editDataMap = (HashMap)editDataCol.iterator().next();
				if(editDataMap.get("codeStatusE").equals("edit")){
					hasEditStatus = true;
				}
			}
		}
		return hasEditStatus;
	}
	
	//get the particular session details
	public String getSessionData(Event evt, String sessionName){
		Collection editDataCol = (Collection)evt.getRequest().getSession().getAttribute("editData");
		HashMap editDataMap = (HashMap)editDataCol.iterator().next();
		String ssName = editDataMap.get(sessionName).toString();
		return ssName;
	}
	
}
