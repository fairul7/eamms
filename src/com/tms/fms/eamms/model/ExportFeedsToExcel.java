package com.tms.fms.eamms.model;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import kacang.Application;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.engineering.ui.ServiceDetailsForm;

public class ExportFeedsToExcel extends HttpServlet 
{
	private String requestId;
	private String feedsDetailsId;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        try
        {
        	requestId = (String) request.getSession().getAttribute("feedsRequestId_forExcel");
        	request.getSession().removeAttribute("feedsRequestId_forExcel");
        	
        	if(requestId != null)
        	{
                SimpleDateFormat sdf    = new SimpleDateFormat("ddMMyyyy_hh:mm_a");
                String timeStamp        = sdf.format(new Date()); 
                
                String filename         = "feeds" + "_" + timeStamp;
                filename = filename.replaceAll(" ", "_");
                
                response.setHeader("Content-type", "application/excel");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xls" );
                
                // create workbook and sheet
                createReportSheet(response.getOutputStream());
        	}
        }
        catch (Exception e)
        {
             Log.getLog(getClass()).error("Error Generating Feeds Excel : " + e, e);
        }
    }
    
    public void createReportSheet(OutputStream outputstream) throws IOException, WriteException 
    {
    	Application app = Application.getInstance();
    	EngineeringModule module= (EngineeringModule)app.getModule(EngineeringModule.class);
    	
        WritableWorkbook wBook = Workbook.createWorkbook(outputstream);
        WritableSheet sheet = wBook.createSheet("feedsReport", 0);

        int row = 0;
        int column = 0;
        
        WritableFont headerFont2         = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, false);
        WritableCellFormat headerCell2   = new WritableCellFormat(headerFont2);

        Label title = new Label(column, ++row, app.getMessage("eamms.feed.msg.feedRequisition"), headerCell2);
        sheet.addCell(title);
        
		EngineeringRequest request = module.getRequestWithService(requestId);
		if(request != null) 
		{
			HashMap fMap = new HashMap();
			fMap.put("0", app.getMessage("fms.facility.label.localFeed"));
			fMap.put("1", app.getMessage("fms.facility.label.foreignFeed"));
			fMap.put("2", app.getMessage("fms.facility.label.visualFeed"));
			
			Collection services = request.getServices();
			for(Iterator itr = services.iterator();itr.hasNext();)
			{
				Service service=(Service)itr.next();
				if(service != null && service.getServiceId().equals(ServiceDetailsForm.SERVICE_TVRO))
				{
					row++;
			        sheet.addCell(new Label(column, ++row, app.getMessage("eamms.feed.list.msg.request") + " : ", headerCell2));
			        sheet.addCell(new Label(++column, row, request.getTitle(), headerCell2));
			        
			        column = 0;
					sheet.addCell(new Label(column, ++row, app.getMessage("fms.facility.label.feedType") + " : ", headerCell2));
					sheet.addCell(new Label(++column, row, (String)fMap.get(service.getFeedType()), headerCell2));
					
					row++;
					column = 0;
					sheet.addCell(new Label(column, row, app.getMessage("fms.facility.label.sNo"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.feedTitle"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.table.location"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.requiredDate"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.requiredTime"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.blockBooking"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.totalTimeReq"), headerCell2));
					sheet.addCell(new Label(++column, row, app.getMessage("fms.facility.label.remarks"), headerCell2));
					
					Collection<TvroService> sCol = module.getTvroServiceIncludeInvalidRateCard(requestId, ServiceDetailsForm.SERVICE_TVRO);
					if(sCol != null && !sCol.isEmpty())
					{
						int i = 1;
						for(TvroService tvroObj : sCol)
						{
							row++;
							column = 0;
							
							sheet.addCell(new Label(column, row, String.valueOf(i++), headerCell2));
							sheet.addCell(new Label(++column, row, tvroObj.getFeedTitle(), headerCell2));
							sheet.addCell(new Label(++column, row, tvroObj.getLocation(), headerCell2));
							
							SimpleDateFormat sdf = new SimpleDateFormat(app.getProperty("globalDateLong"));
							String reqDate = "";
							String reqDateTo = "";
							try
							{
								reqDate = sdf.format(tvroObj.getRequiredDate());
								reqDateTo = sdf.format(tvroObj.getRequiredDateTo());
							}
							catch(Exception e){}
							String d = reqDate + " - " + reqDateTo;
							sheet.addCell(new Label(++column, row, d, headerCell2));
							
							String time = tvroObj.getFromTime() + " - " + tvroObj.getToTime();
							sheet.addCell(new Label(++column, row, time, headerCell2));
							
							String bb = "1".equals(tvroObj.getBlockBooking()) ? "Yes" : "No";
							sheet.addCell(new Label(++column, row, bb, headerCell2));
							
							sheet.addCell(new Label(++column, row, String.valueOf(tvroObj.getTotalTimeReq()) + " "
									+ tvroObj.getTimeMeasureLabel(), headerCell2));
							sheet.addCell(new Label(++column, row, tvroObj.getRemarks(), headerCell2));
						}
					}
					break;
				}
			}
			
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			EammsFeedsDetails fdObj = em.getFeedsRequestDetails(requestId);
			if(fdObj != null)
			{
				feedsDetailsId = fdObj.getId();
				
				row++;
				column = 0;
				sheet.addCell(new Label(column, ++row, "A. " + app.getMessage("eamms.feed.list.msg.requesterDetails").toUpperCase(), headerCell2));
				
				row++;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.ecAssigned"), headerCell2));
				sheet.addCell(new Label(++column, row, (String)fdObj.getProperty("assignedEcStr"), headerCell2));
				
				row++;
				column = 0;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.requestedBy"), headerCell2));
				sheet.addCell(new Label(++column, row, fdObj.getSubmittedBy(), headerCell2));
				
				column += 3;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.staffNo"), headerCell2));
				sheet.addCell(new Label(++column, row, (String)fdObj.getProperty("staffId"), headerCell2));
				
				row++;
				column = 0;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.dept"), headerCell2));
				sheet.addCell(new Label(++column, row, (String)fdObj.getProperty("department"), headerCell2));
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				String requestedDate = "";
				try
				{
					requestedDate = sdf.format(fdObj.getRequestedDate());
				}
				catch(Exception e){}
				column += 3;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.requestedDate"), headerCell2));
				sheet.addCell(new Label(++column, row, requestedDate, headerCell2));
				
				row++;
				column = 0;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.location"), headerCell2));
				sheet.addCell(new Label(++column, row, fdObj.getLocation(), headerCell2));
				
				column += 3;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.program"), headerCell2));
				sheet.addCell(new Label(++column, row, (String)fdObj.getProperty("programName"), headerCell2));
				
				row++;
				column = 0;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.telco"), headerCell2));
				sheet.addCell(new Label(++column, row, fdObj.getTelco(), headerCell2));
				
				column += 3;
				sheet.addCell(new Label(column, row, app.getMessage("eamms.feed.list.msg.obLink"), headerCell2));
				sheet.addCell(new Label(++column, row, fdObj.getOblink(), headerCell2));
			}
			
			Collection assignments = em.getAssignments(requestId, null);
			if(assignments != null && !assignments.isEmpty())
			{
				row++;
				column = 0;
				sheet.addCell(new Label(column, ++row, "B. " + app.getMessage("eamms.feed.list.msg.assignDetail").toUpperCase(), headerCell2));
				
				row++;
				sheet.addCell(new Label(column, row, app.getMessage("fms.facility.label.sNo"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.assignId"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.tvroTitle"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.requiredDate"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.requiredTime"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.totalTimeReq"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.remarks"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.bookingStat"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.networkRemarks"), headerCell2));
				sheet.addCell(new Label(++column, row, app.getMessage("eamms.feed.list.msg.status"), headerCell2));
				
				int j = 1;
				for(Iterator itr = assignments.iterator();itr.hasNext();)
				{
					EammsAssignment assignObj = (EammsAssignment)itr.next();
					
					row++;
					column = 0;
					
					sheet.addCell(new Label(column, row, String.valueOf(j++), headerCell2));
					sheet.addCell(new Label(++column, row, assignObj.getAssignmentId(), headerCell2));
					sheet.addCell(new Label(++column, row, (String)assignObj.getProperty("feedTitle"), headerCell2));
					//sheet.addCell(new Label(++column, row, (String)assignObj.getProperty("requiredDateStr"), headerCell2));
					sheet.addCell(new Label(++column, row, (String)assignObj.getProperty("requiredDateRangeStr"), headerCell2));
					
					String fr = (String)assignObj.getProperty("hourFrStr") + ":" + (String)assignObj.getProperty("minFrStr");
					String to = (String)assignObj.getProperty("hourToStr") + ":" + (String)assignObj.getProperty("minToStr");
					sheet.addCell(new Label(++column, row, fr + " - " + to, headerCell2));
					
					sheet.addCell(new Label(++column, row, (String)assignObj.getProperty("totalReqTime_measure"), headerCell2));
					sheet.addCell(new Label(++column, row, assignObj.getRemarks(), headerCell2));
					sheet.addCell(new Label(++column, row, assignObj.getBookingStatus(), headerCell2));
					sheet.addCell(new Label(++column, row, assignObj.getNetworkRemarks(), headerCell2));
					sheet.addCell(new Label(++column, row, assignObj.getStatus(), headerCell2));
				}
			}
			
			row += 2;
			Collection statusTrailCol = em.getStatusTrail(feedsDetailsId);
			if(statusTrailCol != null && !statusTrailCol.isEmpty())
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
				for(Iterator itr = statusTrailCol.iterator();itr.hasNext();)
				{
					StatusTrail statudTrailObj = (StatusTrail)itr.next();
					
					row++;
					column = 3;
					
					String trail = "";
					String status = statudTrailObj.getStatus();
					String createdBy = statudTrailObj.getCreatedBy();
					
					String createdDate = "";
					try
					{
						createdDate = sdf.format(statudTrailObj.getCreatedDate());
					}
					catch(Exception e){}
					
					trail = status + ", " +  app.getMessage("eamms.feed.list.msg.on") + " " + createdDate + " " +
						app.getMessage("eamms.feed.list.msg.by") + " " + createdBy;
					sheet.addCell(new Label(column, row, trail, headerCell2));
				}
			}
		}
		
        wBook.write();
        wBook.close();
    }
}

	