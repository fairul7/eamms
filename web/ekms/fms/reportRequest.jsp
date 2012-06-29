<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.reports.model.*, com.tms.fms.engineering.model.*, kacang.util.*"%>

<%!	
	public void GenerateOldRequestReport() {
	
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);		
		ReportsFmsDao dao = (ReportsFmsDao)Application.getInstance().getModule(ReportsFmsModule.class).getDao();			
		Collection oldRequestColl = new ArrayList();
		String requestId = null;
		
		ArrayList args = new ArrayList();
		
		args.add(EngineeringModule.ASSIGNMENT_STATUS);
		args.add(EngineeringModule.FULFILLED_STATUS);
		args.add(EngineeringModule.CLOSED_STATUS);
		args.add(EngineeringModule.LATE_STATUS);
		
		try{							
			String sql = 	"SELECT DISTINCT requestId FROM fms_eng_request "+
						 	"WHERE (status = ? OR status = ? OR status = ? OR status = ?) "+
							"AND requestId NOT IN (SELECT requestId FROM fms_eng_resources_report )	 ";			
			
			oldRequestColl = dao.select(sql,HashMap.class, args.toArray(), 0, -1);
			
			int i=0; 
			if(oldRequestColl.size() > 0){		
				for(Iterator it = oldRequestColl.iterator(); it.hasNext(); ){
					Map map = (Map) it.next();
					requestId = (String)map.get("requestId");
					i++;
					
					module.generateReport(requestId);
				}
			}			
		}catch(Exception e){System.out.print("Error reportRequest:"+e);}
		
	}										
%>

<%
GenerateOldRequestReport();
%>

done :)