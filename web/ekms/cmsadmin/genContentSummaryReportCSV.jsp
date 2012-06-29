<%@page import="java.text.SimpleDateFormat,
				java.util.ArrayList,
				java.util.Collection,
				kacang.Application,
				kacang.ui.WidgetManager,
				java.util.Iterator,
				java.text.SimpleDateFormat,
				java.util.Calendar,
                java.text.DateFormat,
				com.tms.report.model.ReportContentObject,
				com.tms.report.ui.ContentSummaryReport,
				com.tms.util.csv.CSVPrinterUtil"
%>

<%
	CSVPrinterUtil csvp = new CSVPrinterUtil();

	

	ContentSummaryReport report = (ContentSummaryReport)Application.getInstance().getWidget(request, "contentSummaryReportPg.contentSummaryReport");
	
	Collection col = report.getResults();
	
	csvp.write(new String[] {"Content Summary Report"} );
    csvp.writeln();
    csvp.writeln();
    
    DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	StringBuffer date = new StringBuffer("From ");
	date.append(sdf.format(report.getStartDate().getDate()));
	date.append(" to ");
	date.append(sdf.format(report.getEndDate().getDate()));
	
	csvp.write(new String[]{date.toString()});
	csvp.writeln();
	csvp.writeln();
	
	csvp.write(new String[] {"Content Type"} );
	csvp.write(new String[] {"Submitted"} );
    csvp.write(new String[] {"Approved"} );
	/*csvp.write(new String[] {"Approved By Supervisor"} );
	csvp.write(new String[] {"Approved By Editor"} );
	csvp.write(new String[] {"Approved By Compliance"} );*/
	csvp.write(new String[] {"Published"} );
	csvp.writeln();
	
	for(Iterator i=col.iterator(); i.hasNext(); ){
		ReportContentObject obj = (ReportContentObject)i.next();
		
		csvp.write(new String[] { obj.getContentName() });
        csvp.write(new String[] { String.valueOf(obj.getSubmittedCount()) });
        csvp.write(new String[] { String.valueOf(obj.getApprovedBySupervisorCount()) });
        /*csvp.write(new String[] { String.valueOf(obj.getApprovedBySupervisorCount()) });
        csvp.write(new String[] { String.valueOf(obj.getApprovedByEditorCount()) });
        csvp.write(new String[] { String.valueOf(obj.getApprovedByComplianceCount()) });*/
        csvp.write(new String[] { String.valueOf(obj.getPublishCount()) });
        csvp.writeln();
	}
	
	
	Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "content_summary_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>