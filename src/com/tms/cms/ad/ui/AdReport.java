package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AdReport extends LightWeightWidget {
    private String errorMessage;
    private String adId;
    private String year;
    private String month;
    private boolean click;
    private boolean validReport;
    private List validYearList;
    private Map dailyReportMap;
    private Map dailyUniqueReportMap;
    private Map monthlyReportMap;
    private Map monthlyUniqueReportMap;


    public void onRequest(Event evt) {
        AdModule module;

        module = (AdModule) Application.getInstance().getModule(AdModule.class);
        setErrorMessage(null);

        // if year not valid, fix it
        try {
            int year = Integer.parseInt(getYear());
            if(year < 1900 || year > 2100) {
                Calendar cal = Calendar.getInstance();
                setYear(Integer.toString(cal.get(Calendar.YEAR)));
            }
        } catch(NumberFormatException e) {
            Calendar cal = Calendar.getInstance();
            setYear(Integer.toString(cal.get(Calendar.YEAR)));
        }

        // if month not valid, fix it
        try {
            int month = Integer.parseInt(getMonth());
            if(month < 1 || month > 12) {
                Calendar cal = Calendar.getInstance();
                setMonth(Integer.toString(cal.get(Calendar.MONTH) + 1));
            }
        } catch(NumberFormatException e) {
            Calendar cal = Calendar.getInstance();
            setMonth(Integer.toString(cal.get(Calendar.MONTH) + 1));
        }

        try {
            setValidReport(module.hasReport(isClick(), getAdId()));
            if(isValidReport()) {
                validYearList = module.getValidReportYears(isClick(), getAdId());
                dailyReportMap = module.getDailyReport(isClick(), false, getAdId(), getYear(), getMonth());
                dailyUniqueReportMap = module.getDailyReport(isClick(), true, getAdId(), getYear(), getMonth());
                monthlyReportMap = module.getMonthlyReport(isClick(), false, getAdId(), getYear());
                monthlyUniqueReportMap = module.getMonthlyReport(isClick(), true, getAdId(), getYear());
            }

        } catch (AdException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
            setErrorMessage(e.getMessage());
        }
    }

    public String getTemplate() {
        return "ad/adReport";
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public Map getDailyReportMap() {
        return dailyReportMap;
    }

    public void setDailyReportMap(Map dailyReportMap) {
        this.dailyReportMap = dailyReportMap;
    }

    public Map getDailyUniqueReportMap() {
        return dailyUniqueReportMap;
    }

    public void setDailyUniqueReportMap(Map dailyUniqueReportMap) {
        this.dailyUniqueReportMap = dailyUniqueReportMap;
    }

    public Map getMonthlyReportMap() {
        return monthlyReportMap;
    }

    public void setMonthlyReportMap(Map monthlyReportMap) {
        this.monthlyReportMap = monthlyReportMap;
    }

    public Map getMonthlyUniqueReportMap() {
        return monthlyUniqueReportMap;
    }

    public void setMonthlyUniqueReportMap(Map monthlyUniqueReportMap) {
        this.monthlyUniqueReportMap = monthlyUniqueReportMap;
    }

    public boolean isValidReport() {
        return validReport;
    }

    public void setValidReport(boolean validReport) {
        this.validReport = validReport;
    }

    public List getValidYearList() {
        return validYearList;
    }

    public void setValidYearList(List validYearList) {
        this.validYearList = validYearList;
    }
}
