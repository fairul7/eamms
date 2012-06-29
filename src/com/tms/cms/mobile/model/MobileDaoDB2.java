package com.tms.cms.mobile.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.util.Log;

public class MobileDaoDB2 extends MobileDao{
	
	public Collection list(String title, int start, int rows) throws DaoException {
        try {
            title = (title != null) ? "%" + title.toUpperCase() + "%" : "%";
            Collection results = super.select("SELECT title, dateCreated, dateModified, maxSize, depth, allowImages, offsiteLinks, refresh, refreshHourlyDuration, refreshDailyTime, contentId " +
                    "FROM cms_mobile_channel WHERE UPPER(title) LIKE ? ORDER BY title", MobileChannel.class, new Object[] {title}, start, rows);
            return results;
        } catch (Exception e) {
            Log.getLog(getClass()).error("Unable to load channels " + e.toString(), e);
            throw new DaoException("Unable to load channels");
        }
    }
	
	public int count(String title) throws DaoException {
        try {
            title = (title != null) ? "%" + title.toUpperCase() + "%" : "%";
            Collection results = super.select("SELECT COUNT(*) AS channelCount FROM cms_mobile_channel WHERE UPPER(title) LIKE ?", HashMap.class, new Object[] {title}, 0, -1);
            HashMap row = (HashMap)results.iterator().next();
            return Integer.parseInt(row.get("channelCount").toString());
        } catch (Exception e) {
        	Log.getLog(getClass()).error("Unable to load channels " + e.toString(), e);
            throw new DaoException("Unable to load channels");
        }
    }

}
