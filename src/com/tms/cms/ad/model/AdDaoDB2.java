package com.tms.cms.ad.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.model.DaoException;
import kacang.util.Log;

/**
 * Data Access Object for the Ad Module.
 */
public class AdDaoDB2 extends AdDao {
	private Log log = Log.getLog(getClass());
	
	public Collection selectAds(String search, String sort, boolean direction, int start, int maxRows) throws AdDaoException {
        Collection ads;
        Iterator iterator;
        Ad ad;

        String sql = "SELECT adId, name, active, url, newWindow, imageFile, " +
        "alternateText, useScript, script, startDate, startDateEnabled, " +
        "endDate, endDateEnabled FROM ad_ad WHERE adId=? OR UPPER(url) LIKE ? OR " +
        "UPPER(name) LIKE ? OR UPPER(alternateText) LIKE ?";

        String sql2 = "SELECT adId, name, active, url, newWindow, imageFile, " +
        "alternateText, useScript, script, startDate, startDateEnabled, " +
        "endDate, endDateEnabled FROM ad_ad";

        if (sort != null) {
            sql = sql + " ORDER BY " + sort;
            if (direction) {
                sql += " DESC";
            }

            sql2 = sql2 + " ORDER BY " + sort;
            if (direction) {
                sql2 += " DESC";
            }
        }

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%"
        };

        if ("*".equals(search)) {
            // returns ALL Ads
            ads = localSelect(sql2, Ad.class, null, start, maxRows);
        } else {
            ads = localSelect(sql, Ad.class, args, start, maxRows);
        }

        // set the contextList for each com.tms.cms.ad
        iterator = ads.iterator();
        while(iterator.hasNext()) {
            ad = (Ad) iterator.next();
            setAdContext(ad);
        }

        return ads;
    }
	
	public int selectAdCount(String search) throws AdDaoException {
        Collection collection;

        String sql = "SELECT COUNT(*) AS total FROM ad_ad WHERE adId=? OR UPPER(url) LIKE ? OR " +
        "UPPER(name) LIKE ? OR UPPER(alternateText) LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ad_ad";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%"
        };

        if ("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, null, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }

        HashMap m = (HashMap) collection.iterator().next();
        return Integer.parseInt(m.get("total").toString());
    }
	
	public Collection selectAdLocations(String search, String sort, boolean direction, int start, int maxRows) throws AdDaoException {
        Collection adLocations;
        Iterator iterator;
        AdLocation al;

        String sql = "SELECT adLocationId, name, active, description, adType " +
        "FROM ad_adlocation WHERE adLocationId=? OR UPPER(name) LIKE ? OR description LIKE ?";
        String sql2 = "SELECT adLocationId, name, active, description, adType " +
        "FROM ad_adlocation";

        if(sort!=null) {
            sql = sql + " ORDER BY " + sort;
            if(direction) {
                sql += " DESC";
            }

            sql2 = sql2 + " ORDER BY " + sort;
            if (direction) {
                sql2 += " DESC";
            }
        }

        Object[] args = new Object[] {
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search + "%"
        };

        if("*".equals(search)) {
            // returns ALL AdLocations
            adLocations = localSelect(sql2, AdLocation.class, null, start, maxRows);
        } else {
            adLocations = localSelect(sql, AdLocation.class, args, start, maxRows);
        }

        // set adlist for each adlocation
        iterator = adLocations.iterator();
        while (iterator.hasNext()) {
            al = (AdLocation) iterator.next();
            setAdListForLocation(al);
        }

        return adLocations;
    }
	
	public int selectAdLocationCount(String search) throws AdDaoException {
        Collection collection;

        String sql = "SELECT COUNT(*) AS total FROM ad_adlocation WHERE adLocationId=? OR UPPER(name) LIKE ? OR description LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ad_adlocation";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search + "%"
        };

        if("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, null, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }

        HashMap m = (HashMap) collection.iterator().next();
        return Integer.parseInt(m.get("total").toString());
    }
	
	private void setAdContext(Ad ad) throws AdDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        List contextList;

        String sql = "SELECT contextId FROM ad_adcontext WHERE adId=?";

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ad.getId());
            rs = pstmt.executeQuery();

            contextList = ad.getContextList();
            while (rs.next()) {
                contextList.add(rs.getString("contextId"));
            }

        } catch (SQLException e) {
            throw new AdDaoException(e);

        } finally {
            doFinally(pstmt, conn);
        }

    }
	
	private void doFinally(PreparedStatement pstmt, Connection conn) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                log.error(e);
            }
        }

        if(conn != null) {
            try {
            conn.close();
            } catch(SQLException e) {
                log.error(e);
            }
        }
    }
	
	private void setAdListForLocation(AdLocation adLocation) throws AdDaoException {
        String sql = "SELECT ad_ad.adId, name, active, url, newWindow, imageFile, " +
        "alternateText, useScript, script, startDate, startDateEnabled, " +
        "endDate, endDateEnabled FROM ad_ad, ad_adlocation_ad WHERE " +
        "ad_ad.adId=ad_adlocation_ad.adId AND ad_adlocation_ad.adLocationId=?";
        Object[] args = { adLocation.getAdLocationId() };

        Collection adCollection = localSelect(sql, Ad.class, args, 0, -1);
        adLocation.setAdList(new ArrayList(adCollection));

        // set active ad list
        List activeAdList = new ArrayList();
        Ad ad;
        for(Iterator iter = adCollection.iterator(); iter.hasNext();) {
            ad = (Ad) iter.next();
            if(ad.isActive()) {
                activeAdList.add(ad);
            }
        }
        adLocation.setActiveAdList(activeAdList);
    }

	
	private Collection localSelect(String sql, Class resultClass, Object args, int start, int maxRows) throws AdDaoException {
        try {
            return super.select(sql, resultClass, args, start, maxRows);
        } catch (DaoException e) {
            throw new AdDaoException(e);
        }
    }
	
}
