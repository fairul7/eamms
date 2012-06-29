 <%@page import="kacang.Application, kacang.model.DefaultDataObject,
				com.tms.fms.facility.model.*,java.util.Collection, java.util.Iterator, java.util.ArrayList"%>
				
	<table border="1">  
			<tr><td>* For Microsoft SQL only (after 2005)</td></tr> 
			<tr><td>* Monitoring Purpose (will list out top 20 slow queries) </td></tr> 
			<tr><td>URL : http://[url]/MSSQL_slowLogQuery.jsp </td></tr> 
			<tr><td>reference : </br>
			1. http://www.flogiston.net/blog/2010/03/01/ms-sql-server-log-slow-queries/ </br>
			2. http://msdn.microsoft.com/en-us/library/ms189741%28v=sql.90%29.aspx </br>
			3. http://msdn.microsoft.com/en-US/library/ms181929%28v=SQL.90%29.aspx </br>
			</td>   
			</tr> 
	</table> 
  <br />
	<table border="1" width="70%"> 
			<tr>
			<td>No.</td>
			<td>total_cpu_time</td>
			<td>total_execution_count</td>
			<td>number_of_statements</td>
			<td>Query Text</td>   
			</tr>
<%  
	Application app = Application.getInstance();
	MonitorModule module = (MonitorModule) app.getModule(MonitorModule.class);
	Collection<DefaultDataObject> col = module.showTop10SlowQuery();  
	int i = 0;
	  	for (Iterator iterate = col.iterator();iterate.hasNext();) {
	  			i++;	
	   			DefaultDataObject s = (DefaultDataObject) iterate.next();  
	   			DefaultDataObject j = module.showHandlerQuery(s);
 			%>
		 		<tr>
		 		<td><%=i%></td>
				<td><%= s.getProperty("total_cpu_time")%></td>
				<td><%=s.getProperty("total_execution_count")%></td>
				<td><%=s.getProperty("number_of_statements")%></td> 
				<td><%=j.getProperty("text")%></td> 
				</tr>
 			<%  
	}
%>  
		</table> 