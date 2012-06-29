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
                com.tms.collab.isr.model.RequestModel"%>

<%
    CSVPrinterUtil csvp = new CSVPrinterUtil();
    
    
        Application app = Application.getInstance();
        
        csvp.write(new String[] {app.getMessage("isr.menu.timeOfResolve")} );
    	csvp.writeln();
        
	    RequestModel requestModel = (RequestModel) Application.getInstance().getModule(RequestModel.class);
    	String departmentName = requestModel.getDeptName(Application.getInstance().getCurrentUser().getId());
		csvp.write(new String[] {departmentName} );
    	csvp.writeln();
    
	    Date fromDate = (Date)session.getAttribute("reportFromDate");
    	Date toDate = (Date)session.getAttribute("reportToDate");
    
	    DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    	csvp.write(new String[]{sdf.format(fromDate) + " - " + sdf.format(toDate)});
	    csvp.writeln();
    	csvp.writeln();
        
        csvp.write(new String[] { app.getMessage("isr.label.type")});
        csvp.write(new String[] { app.getMessage("isr.label.noOfReq") });
        csvp.writeln();
        
        Collection reportDate = (Collection)session.getAttribute("reportData");
        
        for(Iterator i=reportDate.iterator(); i.hasNext();) {
            ReportObject obj = (ReportObject) i.next();
            
            csvp.write(new String[] { obj.getType() });
            csvp.write(new String[] { obj.getNoOfReq() });
            csvp.writeln();
        }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "time_to_resolve_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
