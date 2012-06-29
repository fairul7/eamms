<%@page import="java.text.SimpleDateFormat,
                java.text.DateFormat,
                java.util.ArrayList,
                java.util.Calendar,
                java.util.Collection,
                java.util.Iterator,
                java.util.Map,
                com.tms.util.csv.CSVPrinterUtil,
                com.tms.sam.po.report.model.StatusReportObject,
                kacang.Application,
                java.util.Date,
                com.tms.hr.orgChart.model.OrgChartHandler,
                com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject"%>

<%
    CSVPrinterUtil csvp = new CSVPrinterUtil();
    
    
    Application app = Application.getInstance();
        
    csvp.write(new String[] {app.getMessage("report.label.statusReport")} );
    csvp.writeln();
        
    
    OrgChartHandler orgChartModel = (OrgChartHandler) app.getModule(OrgChartHandler.class);
   
    String reqDept = (String)session.getAttribute("reportReqDept");
    String status = (String)session.getAttribute("reportStatus");
    
    Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
    
    if(reqDept == null || reqDept.equals("")){
    	reqDept = "All Departments";
    }else{
	    for(Iterator i=deptCols.iterator(); i.hasNext();) {
    		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
    		if(obj.getAssociativityId().equals(reqDept)){
	    		reqDept = obj.getCountryDesc() + " - " + obj.getDeptDesc();
    		}
	    }
    }
    
    if(status == null || status.equals("")){
    	status = "All Status";
    }
    
    csvp.write(new String[]{app.getMessage("isr.label.reqDept")});
    csvp.write(new String[]{reqDept});
    csvp.writeln();
    csvp.write(new String[]{app.getMessage("myRequest.label.status")});
    csvp.write(new String[]{status});
    csvp.writeln();
    
    Date fromDate = (Date)session.getAttribute("reportFromDate");
    Date toDate = (Date)session.getAttribute("reportToDate");
    
    DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    csvp.write(new String[]{sdf.format(fromDate) + " - " + sdf.format(toDate)});
    csvp.writeln();
    csvp.writeln();
        
    csvp.write(new String[] { app.getMessage("po.label.department")});
    csvp.write(new String[] { app.getMessage("myRequest.label.status") });
    csvp.write(new String[] { app.getMessage("report.label.totalRequest") });
    csvp.writeln();
    
    Collection statusReport = (Collection)session.getAttribute("reportData");
        
    for(Iterator i=statusReport.iterator(); i.hasNext();) {
    	StatusReportObject obj = (StatusReportObject) i.next();
            
        csvp.write(new String[] { obj.getDeptDesc() });
        csvp.write(new String[] { obj.getStatus() });
        csvp.write(new String[] { obj.getTotalRequest() });
        csvp.writeln();
    }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "status_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
