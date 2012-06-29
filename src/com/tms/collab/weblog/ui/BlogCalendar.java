package com.tms.collab.weblog.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoException;

import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import java.text.SimpleDateFormat;

import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.BlogUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 27, 2004
 * Time: 6:28:17 PM
 * To change this template use Options | File Templates.
 */
public class BlogCalendar extends LightWeightWidget
{
    private String dateStr;
    private Date date;
    private String blogId;
    private Collection archiveDates;
    private Date baseDate;
    private Calendar cal,baseCal;
    private String blogUrl;
    public void onRequest(Event event)
    {
        super.onRequest(event);
        dateStr = event.getRequest().getParameter("date");
        blogId = event.getRequest().getParameter("blogId");
        baseDate = new Date();
        cal = Calendar.getInstance();
        baseCal = Calendar.getInstance();
        baseCal.setTime(baseDate);
        if(dateStr==null||dateStr==""){
            date = new Date();
        }else{
            date = BlogUtil.parseDateUrl(dateStr);
        }
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            if(blogId!=null&&!blogId.equals(""))
                archiveDates = wm.getMonthlyArchiveDate(blogId,date);
        } catch (DaoException e)
        {
            e.printStackTrace();
        }
    }

    public Object[][] getCalendarEntries() {
        Object[][] entries = new Object[6][7];
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        int cMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.DATE, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
            cal.roll(Calendar.DAY_OF_WEEK, false);
        }

        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                if (cal.get(Calendar.MONTH) == cMonth) {
                    entries[week][day] = new Integer(cal.get(Calendar.DATE));
                } else {
                    entries[week][day] = null;
                }
                cal.roll(Calendar.DAY_OF_YEAR, true);
            }
        }

        return entries;
    }

    public String getDefaultTemplate()
    {
        return "weblog/blogcalendar";
    }

    public String getTemplate()
    {
        return getDefaultTemplate();
    }

    public String[] getDaysInWeek(){
        return new String[]{"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public void setDateStr(String dateStr)
    {
        this.dateStr = dateStr;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public Collection getArchiveDates()
    {
        return archiveDates;
    }

    public void setArchiveDates(Collection archiveDates)
    {
        this.archiveDates = archiveDates;
    }

    public boolean isBaseMonth(){
        cal.setTime(date);
        return baseCal.get(Calendar.YEAR)==cal.get(Calendar.YEAR)&&baseCal.get(Calendar.MONTH)==cal.get(Calendar.MONTH);
    }

    public int getBaseDayOfMonth(){
        return baseCal.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth(){
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public String getPreviousUrl(){
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        return BlogUtil.formatDateUrl(cal.getTime());
		
    }
	
    public String getNextUrl(){
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.MONTH,2);
        cal.add(Calendar.DAY_OF_MONTH,-1);
		return BlogUtil.formatDateUrl(cal.getTime());
    }

    public String getTodayUrl(){
        return BlogUtil.formatDateUrl(baseCal.getTime());
    }

    public String getMonthStr(){
        cal.setTime(date);
        return new SimpleDateFormat("MM").format(cal.getTime());
    }
    public String getYearStr(){
        cal.setTime(date);
        return new SimpleDateFormat("yyyy").format(cal.getTime());
    }

    public Date getBaseDate()
    {
        return baseDate;
    }

    public void setBaseDate(Date baseDate)
    {
        this.baseDate = baseDate;
    }

    public Calendar getCal()
    {
        return cal;
    }

    public void setCal(Calendar cal)
    {
        this.cal = cal;
    }

    public Calendar getBaseCal()
    {
        return baseCal;
    }

    public void setBaseCal(Calendar baseCal)
    {
        this.baseCal = baseCal;
    }

    public String getBlogUrl()
    {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl)
    {
        this.blogUrl = blogUrl;
    }

}
