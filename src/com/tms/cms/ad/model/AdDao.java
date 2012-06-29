package com.tms.cms.ad.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.Application;
import org.apache.commons.collections.SequencedHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Data Access Object for the Ad Module.
 */
public class AdDao extends DataSourceDao {
    private Log log = Log.getLog(getClass());

    /**
     * Creates database tables.
     */
    public void init() throws DaoException {
        super.update("CREATE TABLE ad_adlocation(adLocationId VARCHAR(35) " +
                "PRIMARY KEY, name VARCHAR(255) UNIQUE, active CHAR(1), " +
                "description TEXT, adType INTEGER)", null);

        super.update("CREATE TABLE ad_ad(adId VARCHAR(35) PRIMARY KEY, name VARCHAR(255), active CHAR(1), "+
                "url VARCHAR(255), newWindow CHAR(1), " +
                "imageFile VARCHAR(255), alternateText VARCHAR(255), useScript CHAR(1), script TEXT, " +
                "startDate DATE, startDateEnabled CHAR(1), endDate DATE, endDateEnabled CHAR(1))", null);

        super.update("CREATE TABLE ad_adcontext(adId VARCHAR(35) REFERENCES ad_ad(adId), contextId VARCHAR(35))", null);

        // 1-to-MANY
        super.update("CREATE TABLE ad_adlocation_ad(adLocationId VARCHAR(35) REFERENCES ad_adlocation(adLocationId), "+
                "adId VARCHAR(35) REFERENCES ad_ad(adId))", null);

        super.update("CREATE TABLE ad_adview(adId VARCHAR(35) REFERENCES ad_ad(adId), viewDate DATETIME, ip VARCHAR(255))", null);
        super.update("CREATE TABLE ad_adclick(adId VARCHAR(35) REFERENCES ad_ad(adId), clickDate DATETIME, ip VARCHAR(255))", null);
    }


    // --- [ AdLocation ] ------------------------------------------------------

    public void insertAdLocation(AdLocation adLocation) throws AdDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ad_adlocation(adLocationId, name, active, " +
                "description, adType) VALUES(?,?,?,?,?)";

        Object[] args = {
                adLocation.getId(),
                adLocation.getName(),
                new Boolean(adLocation.isActive()),
                adLocation.getDescription(),
                new Integer(adLocation.getAdType())
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            clearAdList(tx, adLocation.getAdLocationId());
            insertAdList(tx, adLocation);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public void updateAdLocation(AdLocation adLocation) throws AdDaoException {
        Transaction tx = null;

        String sql = "UPDATE ad_adlocation SET name=?, active=?, " +
                "description=?, adType=? WHERE adLocationId=?";

        Object[] args = {
                adLocation.getName(),
                new Boolean(adLocation.isActive()),
                adLocation.getDescription(),
                new Integer(adLocation.getAdType()),
                adLocation.getId()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            clearAdList(tx, adLocation.getAdLocationId());
            insertAdList(tx, adLocation);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public void deleteAdLocation(String adLocationId) throws AdDaoException {
        Transaction tx=null;

        String sql = "DELETE FROM ad_adlocation WHERE adLocationId=?";
        String sql2 = "DELETE FROM ad_adlocation_ad WHERE adLocationId=?";
        Object[] args = {adLocationId};

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.update(sql2, args);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public int selectAdLocationCount(String search) throws AdDaoException {
        Collection collection;

        String sql = "SELECT COUNT(*) AS total FROM ad_adlocation WHERE adLocationId=? OR name LIKE ? OR description LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ad_adlocation";

        Object[] args = new Object[]{
                search,
                "%" + search + "%",
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

    public AdLocation selectAdLocation(String adLocationId) throws AdDaoException {

        Collection collection;

        String sql = "SELECT adLocationId, name, active, description, adType " +
                "FROM ad_adlocation WHERE adLocationId=?";
        Object[] args = new Object[]{adLocationId};

        collection = localSelect(sql, AdLocation.class, args, 0, 1);
        if (!collection.iterator().hasNext()) {
            throw new AdDaoException("Ad location not found");
        } else {
            AdLocation al = (AdLocation) collection.iterator().next();
            setAdListForLocation(al);
            return al;
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

    public AdLocation selectAdLocationByName(String name) throws AdDaoException {
        Collection collection;

        String sql = "SELECT adLocationId, name, active, description, adType " +
                "FROM ad_adlocation WHERE name=?";
        Object[] args = new Object[] { name };

        collection = localSelect(sql, AdLocation.class, args, 0, 1);
        if(!collection.iterator().hasNext()) {
            throw new AdDaoException("Ad location " + name + " not found");
        } else {
            AdLocation al = (AdLocation) collection.iterator().next();
            setAdListForLocation(al);
            return al;
        }
    }

    public Collection selectAdLocations(String search, String sort, boolean direction, int start, int maxRows) throws AdDaoException {
        Collection adLocations;
        Iterator iterator;
        AdLocation al;

        String sql = "SELECT adLocationId, name, active, description, adType " +
                "FROM ad_adlocation WHERE adLocationId=? OR name LIKE ? OR description LIKE ?";

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
                "%" + search + "%",
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
    // --- [ Ad ] --------------------------------------------------------------

    public void insertAd(Ad ad) throws AdDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ad_ad(adId, name, active, url, newWindow, " +
                "imageFile, alternateText, useScript, script, startDate, " +
                "startDateEnabled, endDate, endDateEnabled) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] args = {
                ad.getId(),
                ad.getName(),
                new Boolean(ad.isActive()),
                ad.getUrl(),
                new Boolean(ad.isNewWindow()),
                ad.getImageFile(),
                ad.getAlternateText(),
                new Boolean(ad.isUseScript()),
                ad.getScript(),
                ad.getStartDate(),
                new Boolean(ad.isStartDateEnabled()),
                ad.getEndDate(),
                new Boolean(ad.isEndDateEnabled())
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            insertAdContext(tx, ad);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public void updateAd(Ad ad) throws AdDaoException {
        Transaction tx = null;

        String sql = "UPDATE ad_ad SET name=?, active=?, url=?, newWindow=?, " +
                "imageFile=?, alternateText=?, useScript=?, script=?, startDate=?, " +
                "startDateEnabled=?, endDate=?, endDateEnabled=? WHERE adId=?";

        Object[] args = {
                ad.getName(),
                new Boolean(ad.isActive()),
                ad.getUrl(),
                new Boolean(ad.isNewWindow()),
                ad.getImageFile(),
                ad.getAlternateText(),
                new Boolean(ad.isUseScript()),
                ad.getScript(),
                ad.getStartDate(),
                new Boolean(ad.isStartDateEnabled()),
                ad.getEndDate(),
                new Boolean(ad.isEndDateEnabled()),
                ad.getId()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            clearAdContext(tx, ad.getId());
            insertAdContext(tx, ad);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public void deleteAd(String adId) throws AdDaoException {
        Transaction tx = null;
        String sql = "DELETE FROM ad_ad WHERE adId=?";
        Object[] args = {adId};

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            clearAdUsage(tx, adId);
            clearAdContext(tx, adId);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AdDaoException(e);
        }
    }

    public int selectAdCount(String search) throws AdDaoException {
        Collection collection;

        String sql = "SELECT COUNT(*) AS total FROM ad_ad WHERE adId=? OR url LIKE ? OR " +
                "name LIKE ? OR alternateText LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ad_ad";

        Object[] args = new Object[]{
                search,
                "%" + search + "%",
                "%" + search + "%",
                "%" + search + "%"
        };

        if ("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, null, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }
        HashMap m = (HashMap) collection.iterator().next();
        return Integer.parseInt(m.get("total").toString());
    }

    public Ad selectAd(String adId) throws AdDaoException {

        Collection collection;

        String sql = "SELECT adId, name, active, url, newWindow, imageFile, " +
                "alternateText, useScript, script, startDate, startDateEnabled, " +
                "endDate, endDateEnabled FROM ad_ad WHERE adId=?";
        Object[] args = new Object[]{adId};

        collection = localSelect(sql, Ad.class, args, 0, 1);
        if (!collection.iterator().hasNext()) {
            throw new AdDaoException("Ad not found");
        } else {
            Ad ad = (Ad) collection.iterator().next();
            return ad;
        }
    }

    public Collection selectAds(String search, String sort, boolean direction, int start, int maxRows) throws AdDaoException {
        Collection ads;
        Iterator iterator;
        Ad ad;

        String sql = "SELECT adId, name, active, url, newWindow, imageFile, " +
                "alternateText, useScript, script, startDate, startDateEnabled, " +
                "endDate, endDateEnabled FROM ad_ad WHERE adId=? OR url LIKE ? OR " +
                "name LIKE ? OR alternateText LIKE ?";

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
                "%" + search + "%",
                "%" + search + "%",
                "%" + search + "%"
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

    // --- [ Ad list in AdLocation ] -------------------------------------------
    private void clearAdList(Transaction tx, String adLocationId) throws SQLException {
        String sql = "DELETE FROM ad_adlocation_ad WHERE adLocationId=?";
        Object[] args = { adLocationId };
        tx.update(sql, args);
    }

    private void insertAdList(Transaction tx, AdLocation al) throws SQLException {
        List adList;

        String sql = "INSERT INTO ad_adlocation_ad(adLocationId, adId) VALUES(?,?)";
        Object[] args = {al.getAdLocationId(), null};

        adList = al.getAdList();
        if(adList!=null) {
            for (int i = 0; i < adList.size(); i++) {
                args[1] = adList.get(i);
                tx.update(sql, args);
            }
        }
    }

    private void clearAdUsage(Transaction tx, String adId) throws SQLException {
        String sql = "DELETE FROM ad_adlocation_ad WHERE adId=?";
        Object[] args = {adId};
        tx.update(sql, args);
    }

    // --- [ Ad Context ] ------------------------------------------------------
    private void clearAdContext(Transaction tx, String adId) throws SQLException {
        String sql = "DELETE FROM ad_adcontext WHERE adId=?";
        Object[] args = { adId };

        tx.update(sql, args);
    }

    private void insertAdContext(Transaction tx, Ad ad) throws SQLException {
        List contextList;

        String sql = "INSERT INTO ad_adcontext(adId, contextId) VALUES(?,?)";
        Object[] args = {ad.getId(), null};

        contextList = ad.getContextList();
        for (int i = 0; i < contextList.size(); i++) {
            args[1] = contextList.get(i);
            tx.update(sql, args);
        }
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

    // === [ com.tms.cms.ad view report ] ==================================================
    public void insertViewOrClick(boolean click, String adId, Date date, String ip) throws AdDaoException {
        String sql;

        if(click) {
            sql = "INSERT INTO ad_adclick(adId, clickDate, ip) VALUES(?,?,?)";
        } else {
            sql = "INSERT INTO ad_adview(adId, viewDate, ip) VALUES(?,?,?)";
        }
        Object[] args = { adId, date, ip };

        this.localUpdate(sql, args);
    }

    public boolean hasReport(boolean click, String adId) throws AdDaoException {
        Collection col;
        Map map;
        int total;
        String sql;

        if(click) {
            sql = "SELECT COUNT(*) AS total FROM ad_adclick WHERE adId=?";
        } else {
            sql = "SELECT COUNT(*) AS total FROM ad_adview WHERE adId=?";
        }
        Object[] args = { adId };

        col = localSelect(sql, HashMap.class, args, 0, 1);
        map = (HashMap) col.iterator().next();
        total = Integer.parseInt(map.get("total").toString());

        if(total>0) {
            return true;
        } else {
            return false;
        }
    }

    public List selectValidReportYears(boolean click, String adId) throws AdDaoException {
        Collection col;
        Map map;
        List validYearList;
        String year;
        String sql;

        if(click) {
            sql = "SELECT DISTINCT year(clickDate) AS year FROM ad_adclick WHERE adId=? ORDER BY clickDate";
        } else {
            sql = "SELECT DISTINCT year(viewDate) AS year FROM ad_adview WHERE adId=? ORDER BY viewDate";
        }
        Object[] args = { adId };

        col = localSelect(sql, HashMap.class, args, 0, -1);

        validYearList = new ArrayList();
        for(Iterator iter=col.iterator(); iter.hasNext(); ) {
            map = (HashMap) iter.next();
            year = map.get("year").toString();
            validYearList.add(year);
        }
        return validYearList;
    }

    public Map selectDailyReport(boolean click, boolean unique, String adId, String year, String month) throws AdDaoException {
        Collection col;
        Map map;
        Map dailyReportMap;
        String sql;

        if(click) {
            if(unique) {
                sql = "SELECT dayOfMonth(clickDate) AS day, COUNT(DISTINCT ip) AS hits " +
                        "FROM ad_adclick WHERE adId=? AND year(clickDate)=? AND " +
                        "month(clickDate)=? GROUP BY day ORDER BY clickDate";
            } else {
                sql = "SELECT dayOfMonth(clickDate) AS day, COUNT(ip) AS hits " +
                        "FROM ad_adclick WHERE adId=? AND year(clickDate)=? AND " +
                        "month(clickDate)=? GROUP BY day ORDER BY clickDate";
            }
        } else {
            if(unique) {
                sql = "SELECT dayOfMonth(viewDate) AS day, COUNT(DISTINCT ip) AS hits " +
                        "FROM ad_adview WHERE adId=? AND year(viewDate)=? AND " +
                        "month(viewDate)=? GROUP BY day ORDER BY viewDate";
            } else {
                sql = "SELECT dayOfMonth(viewDate) AS day, COUNT(ip) AS hits " +
                        "FROM ad_adview WHERE adId=? AND year(viewDate)=? AND " +
                        "month(viewDate)=? GROUP BY day ORDER BY viewDate";
            }
        }
        Object[] args = { adId, year, month };

        col = localSelect(sql, HashMap.class, args, 0, -1);

        dailyReportMap = new SequencedHashMap();
        for(Iterator iter = col.iterator(); iter.hasNext();) {
            map = (HashMap) iter.next();
            dailyReportMap.put(map.get("day").toString(), map.get("hits").toString());
        }
        return dailyReportMap;
    }

    public Map selectMonthlyReport(boolean click, boolean unique, String adId, String year) throws AdDaoException {
        Collection col;
        Map map;
        Map monthlyReportMap;
        int month;
        String sql;
        String[] monthArray = {
                "-Unknown-", Application.getInstance().getMessage("general.label.january"),
                Application.getInstance().getMessage("general.label.february"),
                Application.getInstance().getMessage("general.label.march"),
                Application.getInstance().getMessage("general.label.april"),
                Application.getInstance().getMessage("general.label.may"),
                Application.getInstance().getMessage("general.label.june"),
                Application.getInstance().getMessage("general.label.july"),
                Application.getInstance().getMessage("general.label.august"),
                Application.getInstance().getMessage("general.label.september"),
                Application.getInstance().getMessage("general.label.october"),
                Application.getInstance().getMessage("general.label.november"),
                Application.getInstance().getMessage("general.label.december")
        };

        if(click) {
            if(unique) {
                sql = "SELECT month(clickDate) AS month, COUNT(DISTINCT ip) AS hits " +
                        "FROM ad_adclick WHERE adId=? AND year(clickDate)=? " +
                        "GROUP BY month ORDER BY clickDate";
            } else {
                sql = "SELECT month(clickDate) AS month, COUNT(ip) AS hits " +
                        "FROM ad_adclick WHERE adId=? AND year(clickDate)=? " +
                        "GROUP BY month ORDER BY clickDate";
            }
        } else {
            if(unique) {
                sql = "SELECT month(viewDate) AS month, COUNT(DISTINCT ip) AS hits " +
                        "FROM ad_adview WHERE adId=? AND year(viewDate)=? " +
                        "GROUP BY month ORDER BY viewDate";
            } else {
                sql = "SELECT month(viewDate) AS month, COUNT(ip) AS hits " +
                        "FROM ad_adview WHERE adId=? AND year(viewDate)=? " +
                        "GROUP BY month ORDER BY viewDate";
            }
        }
        Object[] args = {adId, year};

        col = localSelect(sql, HashMap.class, args, 0, -1);

        monthlyReportMap = new SequencedHashMap();
        for(Iterator iter = col.iterator(); iter.hasNext();) {
            map = (HashMap) iter.next();
            try{
                month = Integer.parseInt(map.get("month").toString());
            } catch(NumberFormatException e) {
                month = 0;
            }
            monthlyReportMap.put(monthArray[month], map.get("hits").toString());
        }
        return monthlyReportMap;
    }

    // --- private methods
    private void localUpdate(String sql, Object args) throws AdDaoException {
        try {
            super.update(sql, args);
        } catch (DaoException e) {
            throw new AdDaoException(e);
        }
    }

    private Collection localSelect(String sql, Class resultClass, Object args, int start, int maxRows) throws AdDaoException {
        try {
            return super.select(sql, resultClass, args, start, maxRows);
        } catch (DaoException e) {
            throw new AdDaoException(e);
        }
    }
}

