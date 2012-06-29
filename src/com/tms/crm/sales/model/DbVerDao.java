package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;
import kacang.util.Log;

public class DbVerDao extends DataSourceDao {
	final float latestVersion = 1.1f;
	
	public void init() {
/*
		//Log.getLog(getClass()).info("init DbVerDao " + latestVersion);
		float currentVersion = getCurrentVersion();
		
		if (currentVersion > latestVersion) {
			Log.getLog(getClass()).error("Invalid version (ver=" + currentVersion + " max=" + latestVersion + ")");
		} else if (currentVersion == latestVersion) {
			//Log.getLog(getClass()).info("Current version " + currentVersion + " is up-to-date");
		} else if (currentVersion == -1.0f) {
			fullUpdate();
		} else {
			incrementalUpdate(currentVersion);
		}
*/
	}
	
	private float getCurrentVersion() {
		float currentVersion = -1;
		try {
			Collection col = super.select("SELECT dbversion FROM sfa_dbversion", HashMap.class, null, 0, 1);
			if (col.size() == 1) {
				HashMap hm = (HashMap) col.iterator().next();
				currentVersion = ((Float) hm.get("dbversion")).floatValue();
			} else {
				Log.getLog(getClass()).error("Version not found");
			}
		} catch (DaoException e) {
			try {
				// try see if the opportunity table exists
				super.select("SELECT opportunityID FROM opportunity", HashMap.class, null, 0, 1);
				currentVersion = 1.0f;
			} catch (DaoException e1) {
				Log.getLog(getClass()).error(e1.toString(), e1);
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		return currentVersion;
	}
	
	private void fullUpdate() {
		Log.getLog(getClass()).info("Full Update to version " + latestVersion);
		Log.getLog(getClass()).error("*** Full Update - Feature not implemented yet ***");
	}
	
	private void incrementalUpdate(float currentVersion) {
		try {
			Log.getLog(getClass()).info("Incremental Update from version " + currentVersion + " to " + latestVersion);
			
			if (currentVersion < 1.1f) {
				super.update("CREATE TABLE `sfa_dbversion` (`dbversion` float NOT NULL default '0') TYPE=MyISAM", null);
				super.update("INSERT INTO sfa_dbversion (dbversion) VALUES (1.0);", null);
				super.update("ALTER TABLE `opportunity` ADD COLUMN `creationDateTime` datetime NOT NULL default '0000-00-00 00:00:00' AFTER `partnerCompanyID`", null);
				super.update("CREATE TABLE `salesgroup` (`id` varchar(255) NOT NULL default '', PRIMARY KEY (`id`)) TYPE=MyISAM;", null);
			}
			
			// update to latest version
			super.update("UPDATE sfa_dbversion SET dbversion = ?", new Object[] {new Float(latestVersion)});
		} catch (DaoException e) {
			Log.getLog(getClass()).info("DbVerDao error: " + e.toString(), e);
		}
	}
}