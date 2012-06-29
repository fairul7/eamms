package com.tms.collab.project;

public class WormsDaoSybase extends WormsDaoMsSql {
	public void init() {
		super.init();
		
		try {
			super.update("ALTER TABLE worms_project ADD clientName VARCHAR(100) DEFAULT '' NULL", null);
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE worms_performance_report_project ADD actualProjectStartDate VARCHAR(100) DEFAULT '' NULL", null);
			super.update("ALTER TABLE worms_performance_report_project ADD actualProjectEndDate VARCHAR(100) DEFAULT '' NULL", null);            
			super.update("ALTER TABLE worms_performance_report_project ADD startVariance VARCHAR(100) DEFAULT '' NULL", null);
			super.update("ALTER TABLE worms_performance_report_project ADD endVariance VARCHAR(100) DEFAULT '' NULL", null);
			super.update("ALTER TABLE worms_performance_report_project ADD estDuration VARCHAR(100) DEFAULT '' NULL", null);
			super.update("ALTER TABLE worms_performance_report_project ADD actDuration VARCHAR(100) DEFAULT '' NULL", null);
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE worms_performance_report_defects ADD severity decimal(11,2) DEFAULT 0.0 NULL", null);
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE worms_performance_report_milestone ADD estVariance VARCHAR(100) DEFAULT '' NULL", null);
			super.update("ALTER TABLE worms_performance_report_milestone ADD actVariance VARCHAR(100) DEFAULT '' NULL", null);
		} catch(Exception e) {
		}
	}
}
