package com.tms.cms.ad.model;

import kacang.model.DaoException;
import kacang.Application;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Data Access Object for the Ad Module.
 */
public class AdDaoMsSql extends AdDao {

    public List selectValidReportYears(boolean click, String adId) throws AdDaoException {
        Collection col;
        Map map;
        List validYearList;
        String year;
        String sql;

        if(click) {
            sql = "SELECT DISTINCT YEAR(clickDate) AS year FROM ad_adclick WHERE adId=? ORDER BY YEAR(clickDate)";
        } else {
            sql = "SELECT DISTINCT YEAR(viewDate) AS year FROM ad_adview WHERE adId=? ORDER BY YEAR(viewDate)";
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
                sql = "SELECT DAY(clickDate) AS day, COUNT(DISTINCT ip) AS hits " +
                "FROM ad_adclick WHERE adId=? AND year(clickDate)=? AND " +
                "month(clickDate)=? GROUP BY DAY(clickDate) ORDER BY DAY(clickDate)";
            } else {
                sql = "SELECT DAY(clickDate) AS day, COUNT(ip) AS hits " +
                "FROM ad_adclick WHERE adId=? AND year(clickDate)=? AND " +
                "month(clickDate)=? GROUP BY DAY(clickDate) ORDER BY DAY(clickDate)";
            }
        } else {
            if(unique) {
                sql = "SELECT DAY(viewDate) AS day, COUNT(DISTINCT ip) AS hits " +
                "FROM ad_adview WHERE adId=? AND year(viewDate)=? AND " +
                "month(viewDate)=? GROUP BY DAY(viewDate) ORDER BY DAY(viewDate)";
            } else {
                sql = "SELECT DAY(viewDate) AS day, COUNT(ip) AS hits " +
                "FROM ad_adview WHERE adId=? AND year(viewDate)=? AND " +
                "month(viewDate)=? GROUP BY DAY(viewDate) ORDER BY DAY(viewDate)";
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
                sql = "SELECT MONTH(clickDate) AS month, COUNT(DISTINCT ip) AS hits " +
                "FROM ad_adclick WHERE adId=? AND year(clickDate)=? " +
                "GROUP BY MONTH(clickDate) ORDER BY MONTH(clickDate)";
            } else {
                sql = "SELECT MONTH(clickDate) AS month, COUNT(ip) AS hits " +
                "FROM ad_adclick WHERE adId=? AND year(clickDate)=? " +
                "GROUP BY MONTH(clickDate) ORDER BY MONTH(clickDate)";
            }
        } else {
            if(unique) {
                sql = "SELECT MONTH(viewDate) AS month, COUNT(DISTINCT ip) AS hits " +
                "FROM ad_adview WHERE adId=? AND year(viewDate)=? " +
                "GROUP BY MONTH(viewDate) ORDER BY MONTH(viewDate)";
            } else {
                sql = "SELECT MONTH(viewDate) AS month, COUNT(ip) AS hits " +
                "FROM ad_adview WHERE adId=? AND year(viewDate)=? " +
                "GROUP BY MONTH(viewDate) ORDER BY MONTH(viewDate)";
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
