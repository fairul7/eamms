<%@ include file="/common/header.jsp" %>
<%@ page import="java.io.*" %> 
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.ServletConfig.*" %> 
<%@ page import="javax.servlet.ServletContext.*" %> 
<%@ page import="javax.servlet.ServletException.*" %> 
   
<html>
<title>Log Report</title>
<body>
  
<link rel="stylesheet" href="<c:url value="/ekms/images/fms2008"/>default.css">
<jsp:include page="includes/header.jsp" />
<% 
		try 
		{ 		    
		File file = new File("c:/LogReport.txt"); 		  
		        InputStreamReader streamReader = 
		                new InputStreamReader(new FileInputStream(file)); 		  
		        BufferedReader br = new BufferedReader(streamReader); 		  
		        String line = new String(); 		        
		        System.out.println(file.getName()); 		        
		        while (br.ready()) { 
		           line = br.readLine(); 
		            System.out.println(line); 
		           	%>
		           	 <td width="777" align="center" valign="top">
      
			<table width="100%" border="0" cellspacing="0" cellpadding="0">	
					<tr><td colspan="2" valign="TOP" bgcolor="#F0F6DD" class="contentBgColor"><font size="2" face="Verdana"><c:out value="<%=line %>"></c:out></FONT><br/>
						
						</table>
						</td>
				        </tr>
				    </tbody>
					  </table>           	
		            <%
		        } 		
		} 
		catch(Exception e) 
		{ 
		out.println(e); 
		} 
%> 
			
</body>
</html>	
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
  
