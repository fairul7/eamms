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
                com.tms.hr.orgChart.model.OrgChartHandler,
                com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject"%>

<%
    CSVPrinterUtil csvp = new CSVPrinterUtil();
    
    
    Application app = Application.getInstance();
        
    csvp.write(new String[] {app.getMessage("isr.menu.priorityReport")} );
    csvp.writeln();
        
    
    OrgChartHandler orgChartModel = (OrgChartHandler) app.getModule(OrgChartHandler.class);
    
    String recDept = (String)session.getAttribute("reportRecDept");
    String reqDept = (String)session.getAttribute("reportReqDept");
    
    Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
    
    if(recDept == null || recDept.equals("")){
    	recDept = "All Departments";
    }else{
	    for(Iterator i=deptCols.iterator(); i.hasNext();) {
    		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
    		if(obj.getAssociativityId().equals(recDept)){
	    		recDept = obj.getCountryDesc() + " - " + obj.getDeptDesc();
    		}
	    }
    }
    
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
    
    csvp.write(new String[]{app.getMessage("isr.label.requestingDept")});
    csvp.write(new String[]{reqDept});
    csvp.writeln();
    csvp.write(new String[]{app.getMessage("isr.label.recDept")});
    csvp.write(new String[]{recDept});
    csvp.writeln();
    
    Date fromDate = (Date)session.getAttribute("reportFromDate");
    Date toDate = (Date)session.getAttribute("reportToDate");
    
    DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    csvp.write(new String[]{sdf.format(fromDate) + " - " + sdf.format(toDate)});
    csvp.writeln();
    csvp.writeln();
        
    csvp.write(new String[] { app.getMessage("isr.label.reqDept")});
    csvp.write(new String[] { app.getMessage("isr.label.recDept") });
    csvp.write(new String[] { app.getMessage("isr.label.adminPriority") });
    csvp.write(new String[] { app.getMessage("isr.label.noOfReq") });
    csvp.writeln();
    
    Collection statusReport = (Collection)session.getAttribute("reportData");
        
    for(Iterator i=statusReport.iterator(); i.hasNext();) {
    	ReportObject obj = (ReportObject) i.next();
            
        csvp.write(new String[] { obj.getReqDept() });
        csvp.write(new String[] { obj.getRecDept() });
        csvp.write(new String[] { obj.getPriorityByAdmin() });
        csvp.write(new String[] { obj.getNoOfReq() });
        csvp.writeln();
    }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "priority_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
