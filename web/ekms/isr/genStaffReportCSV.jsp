<%@page import="java.text.SimpleDateFormat,
                java.text.DateFormat,
                java.util.ArrayList,
                java.util.Calendar,
                java.util.Collection,
                java.util.Iterator,
                java.util.Map,
                com.tms.util.csv.CSVPrinterUtil,
                com.tms.collab.isr.report.model.ReportObject,
                kacang.Application,
                java.util.Date,
                com.tms.collab.isr.model.RequestModel,
                org.apache.commons.collections.SequencedHashMap,
                com.tms.collab.isr.model.StatusObject"%>

<%
    CSVPrinterUtil csvp = new CSVPrinterUtil();
    
    
        Application app = Application.getInstance();
        
        Map optionsMap = new SequencedHashMap();
		
		RequestModel requestModel = (RequestModel) app.getModule(RequestModel.class);
			
		Collection statusCols = requestModel.selectAllStatus();
		for(Iterator i=statusCols.iterator(); i.hasNext(); ) {
			StatusObject status = (StatusObject) i.next();
			optionsMap.put(status.getStatusId(), status.getStatusName());
		}
        
        
        csvp.write(new String[] {app.getMessage("isr.menu.staffReport")} );
    	csvp.writeln();
        
    	String departmentName = requestModel.getDeptName(Application.getInstance().getCurrentUser().getId());
	    csvp.write(new String[] {departmentName} );
    	csvp.writeln();
    
	    Date fromDate = (Date)session.getAttribute("reportFromDate");
    	Date toDate = (Date)session.getAttribute("reportToDate");
    
	    DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	    csvp.write(new String[]{sdf.format(fromDate) + " - " + sdf.format(toDate)});
    	csvp.writeln();
	    csvp.writeln();
        
        csvp.write(new String[] { app.getMessage("isr.label.staffName") });
        csvp.write(new String[] { (String)optionsMap.get(StatusObject.STATUS_ID_NEW) });
        csvp.write(new String[] { (String)optionsMap.get(StatusObject.STATUS_ID_IN_PROGRESS) });
        csvp.write(new String[] { (String)optionsMap.get(StatusObject.STATUS_ID_COMPLETED) });
        csvp.write(new String[] { (String)optionsMap.get(StatusObject.STATUS_ID_CLOSE) });
        csvp.write(new String[] { (String)optionsMap.get(StatusObject.STATUS_ID_REOPEN) });
        csvp.write(new String[] { app.getMessage("isr.label.total") });
        csvp.writeln();
        
        Collection statusReport = (Collection)session.getAttribute("reportData");
        
        if(statusReport != null){
        
	        for(Iterator i=statusReport.iterator(); i.hasNext();) {
	            ReportObject obj = (ReportObject) i.next();
	            
	            csvp.write(new String[] { obj.getStaffName() });
	            csvp.write(new String[] { obj.getNoOfReqNew() });
	            csvp.write(new String[] { obj.getNoOfReqInProgress() });
	            csvp.write(new String[] { obj.getNoOfReqCompleted() });
	            csvp.write(new String[] { obj.getNoOfReqClose() });
	            csvp.write(new String[] { obj.getNoOfReqReopen() });
	            csvp.write(new String[] { obj.getNoOfReq() });
	            csvp.writeln();
	        }
        
        }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "staff_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
