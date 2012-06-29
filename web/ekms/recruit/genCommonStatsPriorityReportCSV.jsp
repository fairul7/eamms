<%@page import="java.text.SimpleDateFormat,
                java.text.DateFormat,
                java.util.ArrayList,
                java.util.Calendar,
                java.util.Collection,
                java.util.Iterator,
                com.tms.hr.recruit.model.CSVPrinterUtil,
                kacang.Application,
                java.util.Date,
                com.tms.hr.recruit.model.VacancyObj,
                com.tms.hr.recruit.model.RecruitModule"
              
%>

<%
    	CSVPrinterUtil csvp = new CSVPrinterUtil();
        Application app = Application.getInstance();
        
        csvp.write(new String[] {app.getMessage("recruit.menu.label.recruitReportAudit")} );
    	csvp.writeln();
       
        csvp.write(new String[] {app.getMessage("recruit.general.label.username")});
        csvp.write(new String[] { app.getMessage("recruit.general.label.vacancyCode") });
		csvp.write(new String[] {app.getMessage("recruit.general.label.applicantName")});
        csvp.write(new String[] { app.getMessage("recruit.general.label.action") });
		csvp.write(new String[] {app.getMessage("recruit.general.label.dateTime")});
        csvp.writeln();   
		
		DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
		RecruitModule rm = (RecruitModule)  app.getModule(RecruitModule.class);
		Collection auditCol = rm.findAllAudit("a.auditDate", false, 0, -1, "", "", "");
		
        for(Iterator i=auditCol.iterator(); i.hasNext();) {
        	VacancyObj obj = (VacancyObj) i.next();
            csvp.write(new String[] { obj.getUsername() });
            csvp.write(new String[] { obj.getVacancyCode() });
            csvp.write(new String[] { obj.getName() });
            csvp.write(new String[] { obj.getActionTaken() });
            csvp.write(new String[] { dmyDateFmt.format(obj.getAuditDate()) });
            csvp.writeln();
        }
        
        csvp.writeln();
        
	    Calendar calendar = Calendar.getInstance();
	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
	    
	    String fileName = "recruitment_report_" + dateFormat.format(calendar.getTime()) + ".csv";
	    
	    //response.setContentType("text/excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>

<%= csvp.getOutputString() %>
