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
				com.tms.report.ui.ContentUsageReport,
				com.tms.util.csv.CSVPrinterUtil"
%>

<%
	CSVPrinterUtil csvp = new CSVPrinterUtil();

	

	ContentUsageReport report = (ContentUsageReport)Application.getInstance().getWidget(request, "contentUsageReportPg.contentUsageReport");
	
	Collection col = report.getResults();
	
	csvp.write(new String[] {"Content Usage Report"} );
    csvp.writeln();
    csvp.writeln();
    
    if(report.getStartDate().getDate() != null && report.getEndDate().getDate() != null){
    	
    	DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    	StringBuffer date = new StringBuffer("From ");
    	date.append(sdf.format(report.getStartDate().getDate()));
    	date.append(" to ");
    	date.append(sdf.format(report.getEndDate().getDate()));
    	
    	csvp.write(new String[]{date.toString()});
    	csvp.writeln();
    	csvp.writeln();
    	
    }
    
	csvp.write(new String[] {"Content"} );
	csvp.write(new String[] {"Group"} );
	csvp.write(new String[] {"No. Of User (unique)"} );
	csvp.write(new String[] {"No. Of User (Total)"} );
	csvp.writeln();
	
	for(Iterator i=col.iterator(); i.hasNext(); ){
		ReportContentObject obj = (ReportContentObject)i.next();
		
		csvp.write(new String[] { obj.getSectionName() });
        csvp.write(new String[] { obj.getGroupName() });
        csvp.write(new String[] { String.valueOf(obj.getUniqueCount()) });
        csvp.write(new String[] { String.valueOf(obj.getTotalCount()) });
        csvp.writeln();
	}
	
	
	Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "content_usage_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>