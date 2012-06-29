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
        
        csvp.write(new String[] {app.getMessage("isr.menu.commonStats")} );
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
        
        csvp.write(new String[] {app.getMessage("isr.label.priority")});
        csvp.write(new String[] { app.getMessage("isr.label.noOfReq") });
        csvp.writeln();
        
        Collection reportCol1 = (Collection)session.getAttribute("reportData1");
        
        for(Iterator i=reportCol1.iterator(); i.hasNext();) {
            ReportObject obj = (ReportObject) i.next();
            
            csvp.write(new String[] { obj.getPriority() });
            csvp.write(new String[] { obj.getNoOfReq() });
            csvp.writeln();
        }
        
        csvp.writeln();
        csvp.write(new String[] {app.getMessage("isr.label.status")});
        csvp.write(new String[] { app.getMessage("isr.label.noOfReq") });
        csvp.writeln();
        
        Collection reportCol2 = (Collection)session.getAttribute("reportData2");
        
        for(Iterator i=reportCol2.iterator(); i.hasNext();) {
            ReportObject obj = (ReportObject) i.next();
            
            csvp.write(new String[] { obj.getStatus() });
            csvp.write(new String[] { obj.getNoOfReq() });
            csvp.writeln();
        }
        
        csvp.writeln();
        csvp.write(new String[] {app.getMessage("isr.label.requestType")});
        csvp.write(new String[] { app.getMessage("isr.label.noOfReq") });
        csvp.writeln();
        
        Collection reportCol4 = (Collection)session.getAttribute("reportData4");
        
        for(Iterator i=reportCol4.iterator(); i.hasNext();) {
            ReportObject obj = (ReportObject) i.next();
            
            csvp.write(new String[] { obj.getRequestType() });
            csvp.write(new String[] { obj.getNoOfReq() });
            csvp.writeln();
        }
        
        csvp.writeln();
        csvp.write(new String[] {app.getMessage("isr.label.dept")});
        csvp.write(new String[] { app.getMessage("isr.label.noOfReqMade") });
        csvp.write(new String[] { app.getMessage("isr.label.noOfReqRec") });
        csvp.writeln();
        
        Collection reportCol3 = (Collection)session.getAttribute("reportData3");
        
        for(Iterator i=reportCol3.iterator(); i.hasNext();) {
            ReportObject obj = (ReportObject) i.next();
            
            csvp.write(new String[] { obj.getDept() });
            csvp.write(new String[] { obj.getReqDeptNoOfReq() });
            csvp.write(new String[] { obj.getRecDeptNoOfReq() });
            csvp.writeln();
        }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "common_stats_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
