<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.transport.model.*, com.tms.fms.setup.model.*"%>

<x:config>
    <page name="drivers">         
            <com.tms.fms.transport.ui.DriverAssignmentList name="table" />        
    </page>
</x:config>


<%
	String flagId = null;
	String userId = null;
	
	TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);	
	TransportRequest tr = new TransportRequest();
	flagId = request.getParameter("flagId");
	if(flagId != null){
	try{	
		tr = TM.getDriverAssignmentsForAdmin(flagId);
		if(!(tr == null)){
			userId = tr.getManpowerId();
			pageContext.setAttribute("userId", userId);
		}
					
		System.out.print(userId);
		
	}catch(Exception e){System.out.println(e);}
	}
	
%>	 



<c:if test="${!empty param.flagId}">
	<c:redirect url="driverAssignment.jsp?flagId=${param.flagId}&userId=${userId}"/> 
</c:if>

<c:if test="${forward.name == 'Add New'}" >
  <c:redirect url="driverAssignment.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='fms.tran.assignmentDriverSearch'/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="drivers" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>