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
				com.tms.report.ui.ResourceUsageReport,
				java.util.Map,
				com.tms.collab.resourcemanager.model.Resource,
				com.tms.report.model.ReportObject,
				org.apache.commons.collections.SequencedHashMap,
				com.tms.util.csv.CSVPrinterUtil"
%>

<%
	CSVPrinterUtil csvp = new CSVPrinterUtil();

	ResourceUsageReport report = (ResourceUsageReport)Application.getInstance().getWidget(request, "resourceUsageReportPg.resourceUsageReport");
	
	Map resultMap = report.getReportMap();
	Collection resourceCol = report.getResourceCol();
	
	csvp.write(new String[] {"Resource Usage Report"} );
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
	
	csvp.write(new String[] {"Resource Name"} );
	
	int j=0;
	int j2=0;
	StringBuffer header = new StringBuffer();
	StringBuffer key = new StringBuffer();
	
	for(int i=report.getStartTime(); i<report.getEndTime(); i++){
		j = i%12;
		j2 = j+1;
		if(j == 0)
			j = 12;
		if(j2 == 0)
			j2 = 12;
		header = new StringBuffer();
		header.append(j + "-" + j2);
		if(i < 12)
			header.append(" am");
		else
			header.append(" pm");
		
		csvp.write(new String[] {header.toString()} );
	}
	
	csvp.writeln();
	
	for(Iterator i=resourceCol.iterator(); i.hasNext();){
		
		Resource re = (Resource) i.next();
		csvp.write(new String[] {re.getName()});
		
		for(int k=report.getStartTime(); k<report.getEndTime(); k++){
			
			key = new StringBuffer();
			key.append(re.getId()+"_"+k);
			
			if(resultMap.get(key.toString())==null)
				csvp.write(new String[]{"-"});
			else
				csvp.write(new String[]{resultMap.get(key.toString()).toString()});
			
		}
		
		csvp.writeln();
	}
	
	Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "resource_usage_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>