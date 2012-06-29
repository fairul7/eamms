<%@page import="java.text.SimpleDateFormat,
                java.text.DateFormat,
                java.util.ArrayList,
                java.util.Calendar,
                java.util.Collection,
                java.util.Iterator,
                java.util.Map,
                com.tms.util.csv.CSVPrinterUtil,
                com.tms.sam.po.model.RatingObject,
                kacang.Application,
                java.util.Date"%>

<%
    CSVPrinterUtil csvp = new CSVPrinterUtil();
    
    
    Application app = Application.getInstance();
        
    csvp.write(new String[] {app.getMessage("report.label.supplierRatingReport")} );
    csvp.writeln();
 
    csvp.write(new String[] { app.getMessage("supplier.label.supp")});
    csvp.write(new String[] { app.getMessage("supplier.label.company") });
    csvp.write(new String[] { app.getMessage("evaluation.label.rating") });
    csvp.writeln();
    
    Collection results = (Collection)session.getAttribute("results");
        
    for(Iterator i=results.iterator(); i.hasNext();) {
    	RatingObject obj = (RatingObject) i.next();
            
        csvp.write(new String[] { obj.getFirstname() });
        csvp.write(new String[] { obj.getCompany() });
        if( obj.getTotalRating()==4 )
        {
        	csvp.write(app.getMessage("po.label.excellent"));
        }else if (obj.getTotalRating()==3){
        	csvp.write(app.getMessage("po.label.good"));
        }else if ( obj.getTotalRating()==2){
        	csvp.write(app.getMessage("po.label.fair"));
        }else if ( obj.getTotalRating()==1){
        	csvp.write(app.getMessage("po.label.poor"));
        }else{
        	csvp.write(app.getMessage("po.label.none"));
        }
        
       
        csvp.writeln();
    }
    
    
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
    
    String fileName = "rating_report_" + dateFormat.format(calendar.getTime()) + ".csv";
    
    //response.setContentType("text/excel");
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
%>
<%= csvp.getOutputString() %>
