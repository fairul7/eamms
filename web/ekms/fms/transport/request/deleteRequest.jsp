 <%@page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.transport.model.*" %>
 
 <%
			String id = null;
			id = request.getParameter("id");			
			TransportRequest  TR = new TransportRequest();
			TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
			if(id != null){
				try{
					TM.deleteTransportAssignmentByRequestId(id);
					TM.deleteVehicles(id);
				}catch(Exception er){
					System.out.print("editRequest.jsp: Error when deleting :"+er);
				}
			}
			
			
			response.sendRedirect("requestListing.jsp");
			%>