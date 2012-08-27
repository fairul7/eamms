package com.tms.fms.eamms.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.util.Log;

public class ExportFeedsLogToCSV extends HttpServlet 
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        try
        {
        	Collection<FeedsLogObject> feedsLogCol = (Collection) request.getSession().getAttribute("feedsLogCol");
        	String filterDateFr = (String) request.getSession().getAttribute("filterDateFr");
        	String filterDateTo = (String) request.getSession().getAttribute("filterDateTo");
        	
        	request.getSession().removeAttribute("feedsLogCol");
        	request.getSession().removeAttribute("filterDateFr");
        	request.getSession().removeAttribute("filterDateTo");
        	
        	if(feedsLogCol != null && !feedsLogCol.isEmpty())
        	{
                SimpleDateFormat sdf    = new SimpleDateFormat("ddMMyyyy_hh:mm_a");
                String timeStamp        = sdf.format(new Date()); 
                
                String filename         = "feeds" + "_" + timeStamp;
                filename = filename.replaceAll(" ", "_");
                
                response.setHeader("Content-type", "application/excel");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv" );
                
                String csvContent = createCSV(feedsLogCol, filterDateFr, filterDateTo);
                response.getOutputStream().println(csvContent);
        	}
        }
        catch (Exception e)
        {
             Log.getLog(getClass()).error("Error Generating Feeds Log csv : " + e, e);
        }
    }
    
    private String createCSV(Collection<FeedsLogObject> feedsLogCol, String filterDateFr, String filterDateTo)
    {
    	Application app = Application.getInstance();
    	String csvContent = "";
    	
    	csvContent += app.getMessage("eamms.feed.log.msg.newsFeedLogg") + "\n";
    	csvContent += app.getMessage("eamms.feed.log.msg.dateFr", "", new Object[]{filterDateFr, filterDateTo}) + "\n\n";
    	
    	Collection head = createCSVHeader();
    	Collection body = createCSVContent(feedsLogCol);
    	csvContent += toCSV(head, body);
    	
    	//csvContent = csvContent.replaceAll("\n", "<br>");
    	return csvContent;
    }
    
    private Collection createCSVHeader()
    {
    	Application app = Application.getInstance();
    	
    	ArrayList<String> colHead = new ArrayList();
    	colHead.add(app.getMessage("eamms.feed.log.msg.date"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.program"));
    	colHead.add(app.getMessage("eamms.feed.list.msg.requestId"));
    	colHead.add(app.getMessage("eamms.feed.list.msg.requestTitle"));
    	colHead.add(app.getMessage("eamms.feed.list.msg.assignId"));
    	colHead.add(app.getMessage("eamms.feed.list.msg.requiredTime"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.location"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.station"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.timeIn"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.timeOut"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.ebNum"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.assAv"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.mcr"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.news"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.stringer"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.telco"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.remarks"));
    	colHead.add(app.getMessage("eamms.feed.log.msg.status"));
    	
    	return colHead;
    }
    
    private Collection createCSVContent(Collection<FeedsLogObject> feedsLogCol)
    {
    	ArrayList<Collection> body = new ArrayList();
    	if(feedsLogCol != null && !feedsLogCol.isEmpty())
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    		for(FeedsLogObject obj : feedsLogCol)
    		{
    			ArrayList<String> colBody = new ArrayList();
    			
    			try
    			{
    				colBody.add(sdf.format(obj.getDate()));
    			}
    			catch(Exception e)
    			{
    				colBody.add("");
    			}
    			
    			colBody.add((String)obj.getProperty("programName"));
    			colBody.add(obj.getRequestId());
    			colBody.add((String)obj.getProperty("title"));
    			colBody.add((String)obj.getProperty("code"));
    			colBody.add((String)obj.getProperty("assign_reqTime"));
    			colBody.add(obj.getLocation());
    			colBody.add((String)obj.getProperty("stationName"));
    			colBody.add(obj.getTimeIn());
    			colBody.add(obj.getTimeOut());
    			colBody.add(obj.getEbNo());
    			colBody.add(obj.getAssAV1() + "/" + obj.getAssAV2());
    			colBody.add((String)obj.getProperty("mcrName"));
    			colBody.add(obj.getNews());
    			colBody.add(obj.getStringer());
    			colBody.add((String)obj.getProperty("telcoName"));
    			colBody.add(obj.getRemarks());
    			colBody.add(obj.getStatus());
    			
    			body.add(colBody);
    		}
    	}
    	return body;
    }
    
    public String toCSV(Collection<String> head, Collection<Collection<String>> body)
    {
    	StringBuffer sb = new StringBuffer();
    	if(head != null && !head.isEmpty())
    	{
    		for(String h : head)
    		{
    			if(sb.length() == 0)
    			{
    				sb.append(h);
    				continue;
    			}
    			sb.append(", " + h );
    		}
    		sb.append("\n");
    	}
    	if(body != null && !body.isEmpty())
    	{
    		for(Collection<String> row : body)
    		{
    			StringBuffer sb2 = new StringBuffer();
    			for(String column : row)
    			{
    				if(sb2.length() == 0)
    				{
    					sb2.append(column);
    					continue;
    				}
    				sb2.append(", " + column );
    			}
    			sb2.append("\n");
    			sb.append(sb2);
    		}
    	}
    	return sb.toString();
    }
}

	