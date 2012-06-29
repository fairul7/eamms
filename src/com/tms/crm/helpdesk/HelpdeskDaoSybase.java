package com.tms.crm.helpdesk;

public class HelpdeskDaoSybase extends HelpdeskDaoMsSql {
	public void init() {
		try {
			super.init();
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE hdk_incident MODIFY createdBy varchar(100) NULL", null);
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE hdk_incident_log ADD receipient TEXT DEFAULT '' NULL", null);
		} catch(Exception e) {
		}
	}
}
