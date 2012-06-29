package com.tms.crm.helpdesk;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.text.DecimalFormat;


public class Notification extends DefaultDataObject	{
	private String id;
	private String userId;
	private Date createdOn;
	private String firstAlert;
	private String subsequentAlert;
	private String alertOccurance;
	private String method1;
	private String method2;
	private String occurance;

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String subject;
	private String description;
	private String companyName;
	private String incidentCode;
	private String severity;
	private String contactedBy;
	private String incidentType;
	private String productName;
	private String companyId;
	private String contactId;

	private String incidentId;
	private String counter;
	private Date alertTime;
	private String contactFirstName;
	private String contactLastName;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getFirstAlert() {
		return firstAlert;
	}

	public void setFirstAlert(String firstAlert) {
		this.firstAlert = firstAlert;
	}

	public String getSubsequentAlert() {
		return subsequentAlert;
	}

	public void setSubsequentAlert(String subsequentAlert) {
		this.subsequentAlert = subsequentAlert;
	}

	public String getAlertOccurance() {
		return alertOccurance;
	}

	public void setAlertOccurance(String alertOccurance) {
		this.alertOccurance = alertOccurance;
	}

	public String getMethod1() {
		return method1;
	}

	public void setMethod1(String method1) {
		this.method1 = method1;
	}

	public String getMethod2() {
		return method2;
	}

	public void setMethod2(String method2) {
		this.method2 = method2;
	}

	public String getOccurance() {
		return occurance;
	}

	public void setOccurance(String occurance) {
		this.occurance = occurance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}


	public Date getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIncidentCode() {
		DecimalFormat df = new DecimalFormat("000");
		String value = df.format(Double.valueOf(incidentCode));
		return "#"+value;
	}

	public void setIncidentCode(String incidentCode) {
		this.incidentCode = incidentCode;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getContactedBy() {
		return contactedBy;
	}

	public void setContactedBy(String contactedBy) {
		this.contactedBy = contactedBy;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName() {
		return contactLastName;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

}
