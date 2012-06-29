package com.tms.collab.isr.setting.model;

import kacang.model.DefaultDataObject;

public class EmailSetting extends DefaultDataObject {
	public static String DAILY_DIGEST = "isr.label.dailyDigest";
	public static String REMARKS = "isr.label.remarks";
	public static String NEW_REQUEST = "isr.label.newRequest";
	public static String RESOLUTION = "isr.label.resolutionMsg";
	public static String ASSIGNMMENT = "isr.label.assignment";
	public static String CLARIFICATION= "isr.label.clarificationMsg";
	public static String CLARIFICATION_REPLY = "isr.label.clarificationReply";
	public static String dailyDigestDefaultSubject ="isr.label.dailyDigestDefaultEmailSubject";
	public static String dailyDigestDefaultBody ="isr.label.dailyDigestDefaultEmailBody";
	public static String remarksDefaultSubject= "isr.label.remarksDefaultSubject";
	public static String remarksDefaultBody="isr.label.remarksDefaultBody";
	public static String newRequestDefaultSubject="isr.label.newRequestDefaultSubject";
	public static String newRequestDefaultBody="isr.label.newRequestDefaultBody";
	public static String resolutionDefaultSubject="isr.label.resolutionDefaultSubject";
	public static String resolutionDefaultBody="isr.label.resolutionDefaultBody";
	public static String clarificationReplyDefaultSubject="isr.label.subjectClarificationReply";
	public static String clarificationDefaultSubject="isr.label.clarificationDefaultSubject";
	public static String clarificationDefaultBody="isr.label.clarificationDefaultBody";
	public static String assigmentDefaultSubject="isr.label.AssignDefaultSubject";
	public static String assignmentDefaultBody="isr.label.AssignDefaultBody";
	
	
	private String emailFor;
	private String emailSubject;
	private String emailBody;
	private String notifyMethod;
	
	
	public String getNotifyMethod() {
		return notifyMethod;
	}
	public void setNotifyMethod(String notifyMethod) {
		this.notifyMethod = notifyMethod;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getEmailFor() {
		return emailFor;
	}
	public void setEmailFor(String emailFor) {
		this.emailFor = emailFor;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
}
